package com.kiosoft2.processor

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.asTypeVariableName
import com.squareup.kotlinpoet.jvm.jvmStatic
import java.io.Serializable
import java.util.Arrays
import java.util.Locale
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.TypeParameterElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@AutoService(Processor::class)
//指定要处理的注解
@SupportedAnnotationTypes("androidx.room.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RoomEntityProcessor: AbstractProcessor() {
    /**
     * Property 类
     */
    private val propertyClass = "com.kiosoft2.api.Property"
    //    lateinit var parameterType:TypeElement
    lateinit var MSG:Messager //日志打印
    lateinit var mElement: Elements //获取节点（类，属性，方法）
    lateinit var mTypes: Types //类信息工具类，包含用于操作TypeMirror的工具方法
    lateinit var mFiler:Filer // 文件生成器， 类 资源 等，就是最终要生成的文件 是需要Filer来完成的
    private var callMirror: TypeMirror? = null
    private var serializable: TypeMirror? = null
    private var bundleMirror: TypeMirror? = null
    private fun Messager.print(msg: String) {
        printMessage(Diagnostic.Kind.NOTE, msg);
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        //初始化
        processingEnv?.apply {
            MSG = messager
            mElement = elementUtils
            mTypes = typeUtils
            mFiler = filer
        }
    }
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if (annotations.isNullOrEmpty()) {
            return false
        }
        //记录实体关系类
        var entityRelationList = arrayListOf<EntityRelation>()
        roundEnv?.getElementsAnnotatedWith(Entity::class.java)?.forEach {item ->
            //记录复合主键
            var compositePK = arrayListOf<String>()
            //记录列名
            var columnNames = arrayListOf<String>()
            //构建kotlin类
            var parameterizedTypeName = ClassName.bestGuess("com.kiosoft2.api.Entity")
                .parameterizedBy( ClassName.bestGuess(item.simpleName.toString()))
            val relationEntityClass = String.format("%s_", item.getSimpleName())
            val builder: TypeSpec.Builder = TypeSpec.classBuilder(relationEntityClass)
                .addSuperinterface(parameterizedTypeName)
                .addAnnotation(Keep::class.java)
                .addModifiers(KModifier.PUBLIC)
            //记录复合主键
            val entity: Entity = item.getAnnotation<Entity>(Entity::class.java)
            if (entity != null && entity.primaryKeys != null && entity.primaryKeys.size > 0) {
                compositePK.addAll(Arrays.asList(*entity.primaryKeys))
            }
            //需要设置为静态的属性集合
            var propertySpecStaticList = mutableListOf<PropertySpec>()
            //生成获取表名的方法
            val tableNameFieldName: String = this@RoomEntityProcessor.generateTableNameFiledAndMethod(builder, item,propertySpecStaticList)
            //处理数据表实体类下的数据表字段属性
            var isGenerateGetIdMethod = false
            for (enclosedElement in item.getEnclosedElements()) {
                if (enclosedElement.kind != ElementKind.FIELD
                    || enclosedElement.getAnnotation<Ignore?>(Ignore::class.java) != null
                ) {
                    continue
                }

                //生成class 类属性
                if (this@RoomEntityProcessor.generateField(builder, tableNameFieldName, enclosedElement, compositePK,propertySpecStaticList)) {
                    isGenerateGetIdMethod = true
                }
                columnNames.add(enclosedElement.simpleName.toString())
            }
            //记录保存实体关系
            entityRelationList.add(
                EntityRelation(
                    item.toString(),
                    item.getEnclosingElement().toString() + "." + relationEntityClass
                )
            )
            //生成entity 实例
            this@RoomEntityProcessor.generateEntityInstanceField(builder, item,propertySpecStaticList)
            //生成实体类class属性和获取方法
            this@RoomEntityProcessor.generateEntityClassAndMethod(builder, item,propertySpecStaticList)
            //生成当前Entity的全部属性参数数组
            this@RoomEntityProcessor.generatePropertyArrayMethod(builder, columnNames,propertySpecStaticList)
            //如果没有主键id
            if (!isGenerateGetIdMethod) {
                generateIdPropertyMethod(builder, null)
            }
            //生成复合主键属性和获取方法
           this@RoomEntityProcessor.generateCompositePKMethod(builder, compositePK,propertySpecStaticList)
            //生成绑定属性值的方法
            this@RoomEntityProcessor.generateBindInsertionAdapterValue(builder, item)
            //生成绑定DeleteAdapter属性值的方法
            this@RoomEntityProcessor.generateBindDeleteAdapterValue(builder, item, compositePK)
            //生成绑定update语句属性值的方法
            this@RoomEntityProcessor.generateBindUpdateAdapterValue(builder, item, compositePK)
            if (!propertySpecStaticList.isEmpty()){
                val companionObjectBuilder = TypeSpec.companionObjectBuilder()
                propertySpecStaticList.forEach {
                    companionObjectBuilder.addProperty(it)
                }
                builder.addType(companionObjectBuilder.build())
            }
            //输出java 文件
            this@RoomEntityProcessor.outPutJavaFile(builder.build(),relationEntityClass, item.enclosingElement.toString())
        }
        //生成实体管理类
        this@RoomEntityProcessor.generateEntityManager(entityRelationList)
        return true
    }
    /**
     * 生成实体管理类
     *
     * @param entityRelations 实体信息关联列表
     */
    private fun generateEntityManager(entityRelations: java.util.ArrayList<EntityRelation>) {
        val mapName = "entityMap"


        //生成泛型
        var parameterizedTypeName = HashMap::class.java.asClassName().parameterizedBy(
            String::class.asTypeName(),//String
            ClassName.bestGuess("com.kiosoft2.api.Entity")
                .parameterizedBy( WildcardTypeName.producerOf(ANY))
        )

        //生成字段
        val propertySpec = PropertySpec.builder(mapName, parameterizedTypeName)
            .mutable() // 设置为可变属性
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL) // 添加修饰符，例如 private
            .initializer(" %T()", HashMap::class.java)
            .build()
        val staticCodeBuilder = CodeBlock.builder()

        for (item in entityRelations) {
            staticCodeBuilder.addStatement(
                "%L.put(%S,%T._instance)",
                mapName,
                item.entityClass,
                ClassName.bestGuess(item.relationEntityClass)
            )
        }
        // 构建java类
        val builder: TypeSpec.Builder = TypeSpec.classBuilder("EntityManager")
            .addModifiers(KModifier.PUBLIC)

        //生成根据实体类全名获取表名的方法
        val methodSpec = FunSpec.builder("getEntity")
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addTypeVariable(TypeVariableName("T"))
            .returns( ClassName.bestGuess("com.kiosoft2.api.Entity")
                .parameterizedBy(  TypeVariableName("T")))
            .addParameter("entityClass", Class::class.java.asClassName().parameterizedBy(
                TypeVariableName("T")
            ))
            .addStatement("return %L.get(entityClass.getName())!! as Entity<T>", mapName)
            .build()
        builder.addType(TypeSpec.companionObjectBuilder().addProperty(propertySpec).addInitializerBlock(staticCodeBuilder.build()).addFunction(methodSpec).build())
        this@RoomEntityProcessor.outPutJavaFile(builder.build(),"EntityManager", "com.kiosoft2.api")
    }

    /**
     * 输出java 文件
     *
     * @param javaClass   java 类
     * @param packageName 包名
     */
    private fun outPutJavaFile(build: TypeSpec,className:String, packageName: String) {
        //创建文件
        FileSpec.builder(packageName,className)
            .addType(build)
            .build()
            .writeTo(mFiler)
        MSG.print("创建文件成功：${className}")
    }

    /**
     * 生成绑定update语句属性值的方法
     *
     * @param builder       TypeSpecBuilder
     * @param entityElement 类节点
     */
    private fun generateBindUpdateAdapterValue(
        builder: TypeSpec.Builder,
        entityElement: Element,
        compositePK: java.util.ArrayList<String>
    ) {
        val valueField = "value"
        val stmtField = "stmt"
        //生成方法
        var methodSpecBuilder = FunSpec.builder("bindUpdateAdapterValue")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .addParameter(stmtField,ClassName.bestGuess("androidx.sqlite.db.SupportSQLiteStatement"))
            .addParameter(valueField,ClassName.bestGuess(entityElement.asType().toString()))
            .addStatement("var bindIndex = 1")
        for (fieldElement in entityElement.enclosedElements) {
            if (fieldElement.kind != ElementKind.FIELD
                || fieldElement.getAnnotation(Ignore::class.java) != null
            ) {
                continue
            }
            handleBindValue(fieldElement, methodSpecBuilder, stmtField, valueField, true, false)
        }
        methodSpecBuilder.addCode("\n//绑定where条件参数\n")
        //生成绑定where条件参数代码
        for (fieldElement in getPKElementList(entityElement, compositePK)) {
            handleBindValue(fieldElement, methodSpecBuilder, stmtField, valueField, false, false)
        }

        builder.addFunction(methodSpecBuilder.build())
    }

    /**
     * 生成绑定DeleteAdapter属性值的方法
     *
     * @param builder       TypeSpecBuilder
     * @param entityElement 类节点
     * @param compositePK   复合主键
     */
    private fun generateBindDeleteAdapterValue(builder: TypeSpec.Builder, entityElement: Element, compositePK: ArrayList<String>) {
        val valueField = "value"
        val stmtField = "stmt"
        //生成方法
        var methodSpecBuilder = FunSpec.builder("bindDeleteAdapterValue")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .addParameter(stmtField,ClassName.bestGuess("androidx.sqlite.db.SupportSQLiteStatement"))
            .addParameter(valueField,ClassName.bestGuess(entityElement.asType().toString()))
            .addStatement("var bindIndex = 1")
        for (fieldElement in this@RoomEntityProcessor.getPKElementList(entityElement, compositePK)) {
            handleBindValue(fieldElement, methodSpecBuilder, stmtField, valueField, false, false)
        }

        builder.addFunction(methodSpecBuilder.build())
    }
    /**
     * 获取主键属性列表
     * @param entityElement 实体节点
     * @param compositePK   复合主键
     * @return 主键属性列表
     */
    private fun getPKElementList(entityElement: Element, compositePK: ArrayList<String>): List<Element> {
        //处理where条件参数
        val primaryKeyElement = mutableListOf<Element>()
        for (fieldElement in entityElement.enclosedElements) {
            if (fieldElement.kind != ElementKind.FIELD
                || fieldElement.getAnnotation(Ignore::class.java) != null
            ) {
                continue
            }

            //是否属于主键Id
            if (fieldElement.getAnnotation<PrimaryKey?>(PrimaryKey::class.java) != null) {
                primaryKeyElement.add(fieldElement)
                break
            }

            //字段列信息
            var columnName: String? = null
            val columnInfo = fieldElement.getAnnotation(ColumnInfo::class.java)
            if (columnInfo != null) {
                columnName = columnInfo.name
            }
            if (columnName == null) {
                columnName = fieldElement.simpleName.toString()
            }
            val index = compositePK.indexOf(columnName)
            if (index >= 0) {
                primaryKeyElement.add(fieldElement)
            }
        }

        return primaryKeyElement
    }

    /**
     * 生成绑定InsertionAdapter属性值的方法
     *
     * @param builder       TypeSpecBuilder
     * @param entityElement 类节点
     */
    private fun generateBindInsertionAdapterValue(builder: TypeSpec.Builder, entityElement: Element) {
        var valueField = "value"
        var stmtField = "stmt"
        //生成方法
        var methodSpecBuilder = FunSpec.builder("bindInsertionAdapterValue")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .addParameter(stmtField,ClassName.bestGuess("androidx.sqlite.db.SupportSQLiteStatement"))
            .addParameter(valueField,ClassName.bestGuess(entityElement.asType().toString()))
            .addStatement("var bindIndex = 1")
        for (fieldElement in entityElement.enclosedElements) {
            if (fieldElement.kind != ElementKind.FIELD
                || fieldElement.getAnnotation(Ignore::class.java) != null
            ) {
                continue
            }
            this@RoomEntityProcessor.handleBindValue(fieldElement, methodSpecBuilder, stmtField, valueField, true, true)
        }
        builder.addFunction(methodSpecBuilder.build())
    }
    /**
     * 处理绑定value逻辑
     */
    private fun handleBindValue(fieldElement: Element, methodSpecBuilder: FunSpec.Builder, stmtField: String, valueField: String, isBindNull: Boolean, isUseDefaultValue: Boolean) {
        val propertyField = fieldElement.simpleName.toString()
        val upperFirstPropertyField = propertyField.substring(0, 1)
            .uppercase(Locale.getDefault()) + propertyField.substring(1)
        var bindType: String
        var isBoolean = false
        var isPacking = false
        val isPublic = fieldElement.modifiers.contains(Modifier.PUBLIC)
        var isElse = false
        var bindValueTemplate: String

        when (fieldElement.asType().toString()) {
            "java.lang.Boolean" -> {
                isPacking = true
                bindType = "bindLong"
                isBoolean = true
                bindValueTemplate =
                    String.format("if(\"1\".equals(%s.defaultValue))  1 else 0", propertyField)
            }

            "boolean" -> {
                bindType = "bindLong"
                isBoolean = true
                bindValueTemplate =
                    String.format("if(\"1\".equals(%s.defaultValue) ) 1 else 0", propertyField)
            }

            "java.lang.Integer", "java.lang.Long", "java.lang.Short" -> {
                isPacking = true
                bindType = "bindLong"
                bindValueTemplate = String.format("%s.defaultValue.toLong()", propertyField)
            }

            "int", "long", "short" -> {
                bindType = "bindLong"
                bindValueTemplate = String.format("%s.defaultValue.toLong()", propertyField)
            }

            "java.lang.Byte" -> {
                isPacking = true
                bindType = "bindBlob"
                bindValueTemplate = String.format("%s.defaultValue.toByteArray()", propertyField)
            }

            "byte" -> {
                bindType = "bindBlob"
                bindValueTemplate = String.format("%s.defaultValue.toByteArray()", propertyField)
            }

            "java.lang.Float", "java.lang.Double" -> {
                isPacking = true
                bindType = "bindDouble"
                bindValueTemplate =
                    String.format("%s.defaultValue.toDouble()", propertyField)
            }

            "float", "double" -> {
                bindType = "bindDouble"
                bindValueTemplate =
                    String.format("%s.defaultValue.toDouble()", propertyField)
            }

            else -> {
                isPacking = true
                bindType = "bindString"
                bindValueTemplate = String.format("%s.defaultValue", propertyField)
            }
        }
        if (isBindNull && isPacking) {
            isElse = true
            val codeBuilder: CodeBlock.Builder = CodeBlock.builder()
                .add(
                    if (isPublic) "if(%L.%L == null){\n" else if (isBoolean) "if(%L.is%L() == null){\n" else "if(%L.get%L() == null){\n",
                    valueField,
                    if (isPublic) propertyField else upperFirstPropertyField
                )
            if (isUseDefaultValue) {
                codeBuilder.add("  if(%L.defaultValue != null){\n", propertyField)
                codeBuilder.addStatement(
                    "    " + if (isPublic) "%L.%L(bindIndex++, $bindValueTemplate);" else "%L.%L(bindIndex++, $bindValueTemplate)",
                    stmtField,
                    bindType
                )
                codeBuilder
                    .add("  }else{\n")
                    .add("    %L.bindNull(bindIndex++);}\n", stmtField)
            } else {
                codeBuilder.add("   %L.bindNull(bindIndex++);", stmtField)
            }
            codeBuilder.add("  \n}else{\n")

            methodSpecBuilder.addCode(codeBuilder.build())
        }
        if (isBoolean) {
            methodSpecBuilder.addCode(
                (if (isElse) "   " else "") + (if (isPublic) "%L.%L(bindIndex++, if(%L.%L) 1 else 0);" else "%L.%L(bindIndex++, if (%L.is%L()) 1 else 0);") + if (isElse) "\n}\n" else "\n",
                stmtField,
                bindType,
                valueField,
                if (isPublic) propertyField else upperFirstPropertyField
            )
        } else {
            var str = "%L.%L(bindIndex++, %L.get%L())"
            when(bindType){
                "bindString" -> {
                    str = "%L.%L(bindIndex++, " +
                            "%L.get%L().toString()" +
                            ")"
                }
                "bindBlob" -> {
                    str = "%L.%L(bindIndex++, " +
                            "byteArrayOf(%L.get%L())" +
                            ")"
                }
                "bindNull" ->{
                    str = "%L.%L(bindIndex++, " +
                            "%L.get%L().toInt()" +
                            ")"
                }
                "bindDouble" ->{
                    str = "%L.%L(bindIndex++, " +
                            "%L.get%L().toDouble()" +
                            ")"
                }
                "bindLong" ->{
                    str = "%L.%L(bindIndex++, " +
                            "%L.get%L().toLong()" +
                            ")"
                }
            }
            methodSpecBuilder.addCode(
                (if (isElse) "   " else "") + (if (isPublic) "%L.%L(bindIndex++, %L.%L);" else str) + if (isElse) "\n}\n" else "\n",
                stmtField,
                bindType,
                valueField,
                if (isPublic) propertyField else upperFirstPropertyField
            )
        }
    }

    /**
     * 生成复合主键获取方法
     *
     * @param builder     TypeSpecBuilder
     * @param compositePK 复合主键列表
     */
    private fun generateCompositePKMethod(builder: TypeSpec.Builder, compositePK: ArrayList<String>,propertySpecStaticList:MutableList<PropertySpec>) {
        //处理复合主键名
        val stringBuilder = java.lang.StringBuilder()
        for (item in compositePK) {
            stringBuilder.append(item).append(",")
        }
        val names: String
        names = if (stringBuilder.length > 0) {
            stringBuilder.substring(0, stringBuilder.length - 1)
        } else {
            stringBuilder.toString()
        }
        //生成泛型
        var parameterizedTypeName = ClassName("kotlin.collections", "MutableList").parameterizedBy(ClassName.bestGuess(propertyClass)).copy(nullable = true)
        //生成字段
        val propertySpec = PropertySpec.builder("compositePK_", parameterizedTypeName)
            .mutable() // 设置为可变属性
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL) // 添加修饰符，例如 private
        if (names == null || "" == names) {
            propertySpec.initializer("null")
        } else {
            propertySpec.initializer("%T.asList(%L)", Arrays::class.java, names)
        }
        val build = propertySpec.build()
//        builder.addProperty(propertySpec.build())
        propertySpecStaticList.add(build)
        //生成方法
        var funSpec = FunSpec.builder("getCompositePK")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .returns(parameterizedTypeName)
            .addStatement("return %L", build.name)
            .build()
        builder.addFunction(funSpec)
    }

    /**
     * 生成获取属性数组方法
     *
     * @param builder    TypeSpecBuilder
     * @param properties entity内全部属性列表
     */
   private fun generatePropertyArrayMethod(
        builder: TypeSpec.Builder,
        properties: java.util.ArrayList<String>,
        propertySpecStaticList:MutableList<PropertySpec>
    ) {
        //处理列名
        val stringBuilder = StringBuilder()
        for (item in properties) {
            stringBuilder.append(item).append(",")
        }
        val names: String
        names = if (stringBuilder.length > 0) {
            stringBuilder.substring(0, stringBuilder.length - 1)
        } else {
            stringBuilder.toString()
        }
        //生成泛型
        var parameterizedTypeName = ClassName("kotlin.collections", "MutableList").parameterizedBy(ClassName.bestGuess(propertyClass))
        //生成字段
        val propertySpec = PropertySpec.builder("allProperties_", parameterizedTypeName)
            .mutable() // 设置为可变属性
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL) // 添加修饰符，例如 private
            .initializer("%T.asList(%L)", Arrays::class.java,names)
            .build();
//        builder.addProperty(propertySpec)
        propertySpecStaticList.add(propertySpec)
        //生成方法
        var funSpec = FunSpec.builder("getAllProperties")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .returns(parameterizedTypeName)
            .addStatement("return %L", propertySpec.name)
            .build()
        builder.addFunction(funSpec)
    }

    /**
     * 生成实体类class
     *
     * @param builder       TypeSpecBuilder
     * @param entityElement 类节点
     */
    private fun generateEntityClassAndMethod(builder: TypeSpec.Builder, entityElement: Element,propertySpecStaticList:MutableList<PropertySpec>) {

        val parameterizedBy = Class::class.java.asClassName().parameterizedBy(
            WildcardTypeName.producerOf(ANY.copy(nullable = true))
        )
        //生成字段
        val propertySpec = PropertySpec.builder("entityClass_", parameterizedBy)
            .mutable() // 设置为可变属性
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL) // 添加修饰符，例如 private
            .initializer("%L::class.java", entityElement.simpleName)
            .build();
//        builder.addProperty(propertySpec)
//        builder.addType(TypeSpec.companionObjectBuilder().addProperty(propertySpec).build())
        propertySpecStaticList.add(propertySpec)
        //生成方法
        var funSpec = FunSpec.builder("getEntityClass")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .returns(parameterizedBy)
            .addStatement("return %L", propertySpec.name)
            .build()
        builder.addFunction(funSpec)
    }

    /**
     * 生成entity 实例
     *
     * @param builder       TypeSpec
     * @param entityElement 实体类节点
     */
    private fun generateEntityInstanceField(builder: TypeSpec.Builder, entityElement: Element,propertySpecStaticList: MutableList<PropertySpec>){
        val asTypeVariableName =
            TypeVariableName.invoke(String.format("%s_", entityElement.simpleName));
        //生成获取表的属性
        val propertySpec = PropertySpec.builder("_instance", asTypeVariableName)
            .mutable() // 设置为可变属性
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL) // 添加修饰符，例如 private
            .initializer(" %L()", asTypeVariableName.name)
            .build();
        propertySpecStaticList.add(propertySpec)
    }

    /**
     * 生成class 类属性
     *
     * @param builder            TypeSpecBuilder
     * @param tableNameFieldName 表名属性名
     * @param element            字段节点
     * @return 生成的属性是否属于主键id
     */
    private fun generateField(
        builder: TypeSpec.Builder,
        tableNameFieldName: String,
        element: Element,
        compositePK: ArrayList<String>,
        propertySpecStaticList: MutableList<PropertySpec>
    ): Boolean {
       //是否属于主键Id
        var isId = element.getAnnotation<PrimaryKey>(PrimaryKey::class.java) != null
        //字段列信息

        //字段列信息
        var columnName: String? = null
        var defaultValue: String? = null
        val columnInfo = element.getAnnotation(ColumnInfo::class.java)
        if (columnInfo != null) {
            columnName = columnInfo.name
            defaultValue = columnInfo.defaultValue
            if (defaultValue == null || "" == defaultValue || "[value-unspecified]" == defaultValue) {
                defaultValue = null
            }
        }
        if (columnName == null || "[field-name]" == columnName) {
            columnName = element.simpleName.toString()
        } else {
            val index = compositePK.indexOf(columnName)
            if (index >= 0) {
                compositePK[index] = element.simpleName.toString()
            }
        }
        //生成字段
        val typeName: TypeName = ClassName.bestGuess(propertyClass)
        val fieldSpec = PropertySpec.builder(element.simpleName.toString(),typeName)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .initializer(
                " %T(%L,%S,%S,%T::class.java,%L,%L)",
                typeName,
                tableNameFieldName,
                element.simpleName,
                columnName,
                element.asType(),
                isId,
                if (defaultValue == null || "" == defaultValue) null else String.format(
                    "\"%s\"",
                    defaultValue
                )
            )
            .build()
        //builder.addProperty(fieldSpec)
        propertySpecStaticList.add(fieldSpec)
        //如果是主键id 属性 ，生成获取主键id属性的方法
        if (isId) {
            this@RoomEntityProcessor.generateIdPropertyMethod(builder, fieldSpec)
        }

        return isId
    }
    /**
     * 生成getIdProperty方法
     *
     * @param builder   TypeSpecBuilder
     * @param fieldSpec FieldSpec
     */
    private fun generateIdPropertyMethod(builder: TypeSpec.Builder, fieldSpec: PropertySpec?) {
        //生成方法
        val methodSpec = FunSpec.builder("getIdProperty")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .returns(ClassName.bestGuess(propertyClass))
        if (fieldSpec != null){
            methodSpec.addStatement("return %L", fieldSpec.name)
        } else {
            methodSpec.addStatement("return null")
        }
        builder.addFunction(methodSpec.build())
    }

    /**
     * 生成获取表名属性和方法
     *
     * @param builder       TypeSpecBuilder
     * @param entityElement 类节点
     * @return 表名
     */
    private fun generateTableNameFiledAndMethod(builder: TypeSpec.Builder, entityElement: Element,propertySpecStaticList:MutableList<PropertySpec>): String {
        val entity = entityElement.getAnnotation(Entity::class.java)
        var tableName: String? = null
        if (entity != null) {
            tableName = entity.tableName
        }
        if (tableName == null || "" == tableName) {
            tableName = entityElement.simpleName.toString()
        }

        //生成获取表的属性
        val propertySpec = PropertySpec.builder("tableName_", String::class)
            .mutable() // 设置为可变属性
            .addModifiers(KModifier.PRIVATE, KModifier.FINAL) // 添加修饰符，例如 private
            .initializer("%S", tableName) // 使用 %S 表示字符串
            .build()
//        builder.addProperty(propertySpec)
        propertySpecStaticList.add(propertySpec)
        //生成获取表名的方法
        val methodSpec = FunSpec.builder("getTableName")
            .addModifiers(KModifier.PUBLIC,KModifier.OVERRIDE)
            .returns(ClassName("kotlin", "String"))
            .addStatement("return %L", propertySpec.name)
            .build()
        builder.addFunction(methodSpec)
        return propertySpec.name
    }


}