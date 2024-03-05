package com.kiosoft2.processor

import com.google.auto.service.AutoService
import com.kiosoft2.annotation.AParameter
import com.kiosoft2.processor.config.AProcessorConfig
import com.kiosoft2.processor.config.AProcessorConfig.ACTIVITY_PACKAGE
import com.kiosoft2.processor.config.AProcessorConfig.BUNDLE
import com.kiosoft2.processor.config.AProcessorConfig.Fragment_PACKAGE
import com.kiosoft2.processor.config.AProcessorConfig.METHOD_APARAMETER_GET
import com.kiosoft2.processor.config.AProcessorConfig.PARAMETER_NAME
import com.kiosoft2.processor.config.AProcessorConfig.PARAMETE_FILE_NAME
import com.kiosoft2.processor.config.AProcessorConfig.SERIALIZABLE

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.jvm.jvmOverloads
import java.lang.RuntimeException
import java.util.ArrayList
import java.util.HashMap
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
@AutoService(Processor::class)
//指定要处理的注解
@SupportedAnnotationTypes(AProcessorConfig.APARAMETER)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AParameterProcessor: AbstractProcessor() {
    lateinit var activityType:TypeElement
    lateinit var fragmentType:TypeElement
//    lateinit var parameterType:TypeElement
    lateinit var MSG:Messager //日志打印
    lateinit var mElement: Elements //获取节点（类，属性，方法）
    lateinit var mTypes: Types //类信息工具类，包含用于操作TypeMirror的工具方法
    lateinit var mFiler:Filer // 文件生成器， 类 资源 等，就是最终要生成的文件 是需要Filer来完成的
    private var serializable: TypeMirror? = null
    private var bundleMirror: TypeMirror? = null
    private fun Messager.print(msg: String) {
        printMessage(Diagnostic.Kind.NOTE, msg);
    }
    var aptPackage = AProcessorConfig.APT_PACKAGE_DEFULT//存放apt生成的类的目录
    //<类，<属性集合>>
    private val parameterMap = HashMap<TypeElement, MutableList<Element>>()

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        //初始化
        processingEnv?.apply {
            MSG = messager
            MSG.print("参数注解处理器初始化开始")
            mElement = elementUtils
            mTypes = typeUtils
            mFiler = filer
        }
        serializable = mElement.getTypeElement(SERIALIZABLE).asType()
        bundleMirror = mElement.getTypeElement(BUNDLE).asType()
    }
    @Suppress("SuspiciousIndentation")
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        activityType = mElement.getTypeElement(ACTIVITY_PACKAGE)
        fragmentType = mElement.getTypeElement(Fragment_PACKAGE)
//        parameterType = mElement.getTypeElement(APARAMETER)
        if (null != annotations && !annotations.isEmpty()){
            //获取被@AParameter 标注的所有元素
          var parameterElements = roundEnv?.getElementsAnnotatedWith(AParameter::class.java)
            if (null != parameterElements && !parameterElements.isEmpty()){
                parameterElements.forEach {
                    //获取字段所在的类
                    // element == name， sex,  age
                    // 字段节点的上一个节点 类节点==Key
                    // 注解在属性的上面，属性节点父节点 是 类节点
                    val enclosingElement = it.getEnclosingElement() as TypeElement
                    if (parameterMap.containsKey(enclosingElement)){//当前集合中是否包含Mainactivity这个类
                        parameterMap.get(enclosingElement)!!.add(it)
                    }else{
                        val parameters: MutableList<Element> = ArrayList()
                        // 没有key Personal_MainActivity
                        parameters.add(it)
                        parameterMap.put(enclosingElement,parameters)
                    }
                }
                if (parameterMap.isEmpty()) return true
                createClassFile()//创建辅助类文件
            }
        }
        return true
    }

    private fun createClassFile() {
        //        class MainActivityParameter : ParameterGet {
//            fun getParameter(targetParameter: Any) {
//                val t: MainActivity = targetParameter as MainActivity
//                t.orderDrawable =  RouterManager.getInstance().build("/order/getDrawable").navigation(t) as OrderDrawable
//                t.iUser = RouterManager.getInstance().build("/order/getUserInfo").navigation(t) as IUser
//            }
//        }
        parameterMap.forEach { t, u ->
            var type = 0
            if (mTypes.isSubtype(t.asType(),activityType.asType())){
                type = 1
            }else if (mTypes.isSubtype(t.asType(),fragmentType.asType())){
                type = 2
            }
            if (type == 0){
                throw RuntimeException("@AParameter注解目前仅限用于Activity/Fragment类之中")
            }
            //创建方法
            //   override fun getParameter(targetParameter: Any) {}
            var funSpecBuilder = FunSpec.builder(METHOD_APARAMETER_GET)
                .addModifiers(KModifier.OVERRIDE)//添加override关键子
                .addParameter("targetParameter",Any::class.asTypeName())
            // 是Activity
            // 获取类名 == Personal_MainActivity
            val className: ClassName = t.asClassName()
            funSpecBuilder.addStatement("val t = " + PARAMETER_NAME +" as %T",className)
            u.forEach {
                //获取属性类型
                var fieldType = it.asType().kind.ordinal
                var code = "t.${it.simpleName}"
                var methodContent = if(type == 1) code + " = t.intent." else code + " = t.arguments?."
                println("it.asType().toString()==="+it.asType().toString())
                when(fieldType){
                    TypeKind.INT.ordinal ->{
                        if (type == 1) {
                            methodContent += "getIntExtra(%S,0)"
                        }else{
                            methodContent += "getInt(%S)"
                        }
                        funSpecBuilder.addStatement(methodContent,it.simpleName)
                    }
                    TypeKind.BOOLEAN.ordinal ->{
                        if (type == 1) {
                            methodContent += "getBooleanExtra(%S,false)"
                        }else{
                            methodContent += "getBoolean(%S)"
                        }
                        funSpecBuilder.addStatement(methodContent,it.simpleName)
                    }
                    TypeKind.ARRAY.ordinal ->{
                        if (type == 1) {
                            methodContent += "getParcelableArrayListExtra<Bundle>(%S)"
                        }else{
                            methodContent += "getParcelableArrayList<Bundle>(%S)"
                        }
                        funSpecBuilder.addStatement(methodContent,it.simpleName)
                    }
                    else -> {//String
                        if (it.asType().toString().equals(AProcessorConfig.STRING)){
                            if (type == 1) {
                                methodContent += "getStringExtra(%S).toString()"
                            }else{
                                methodContent += "getString(%S)"
                            }
                            funSpecBuilder.addStatement(methodContent,it.simpleName)
                        }else  if (mTypes.isSubtype(it.asType(),serializable)){
                            //实现了Serializable接口
                            if(type ==1) {
                                methodContent += "getSerializableExtra(%S) as %T"
                            }else{
                                methodContent += "getSerializable(%S) as %T"
                            }
                            funSpecBuilder.addStatement(methodContent,it.simpleName,it.asType().asTypeName())
                        }else if (mTypes.isSubtype(it.asType(),bundleMirror)){
                            if(type ==1) {
                                methodContent += "getBundleExtra(%S)"
                            }else{
                                methodContent += "getBundle(%S)"
                            }
                            funSpecBuilder.addStatement(methodContent,it.simpleName)
                        }else if (it.asType().toString().equals(AProcessorConfig.LIST_BUNDLE)){
                            if(type ==1) {
                                methodContent += "getParcelableArrayListExtra<%T>(%S)!!"
                            }else{
                                methodContent += "getParcelableArrayList<%T>(%S)!!"
                            }
                            funSpecBuilder.addStatement(methodContent,bundleMirror!!.asTypeName(),it.simpleName)
                        }

                    }
                }


            }

            //创建类
            //继承的接口
            var superInterface = ClassName(AProcessorConfig.API_PACKAGE, AProcessorConfig.APARAMETERGET)
            //生成class文件 AParameter$$MainActivity
            var clazName = String.format(PARAMETE_FILE_NAME+t.simpleName.toString())
            //创建类文件
            var typeSpac = TypeSpec.classBuilder(clazName)
                .addFunction(funSpecBuilder.build())
                .addSuperinterface(superInterface)
                .build()
            //创建文件
            FileSpec.builder(aptPackage!!,clazName)
                .addType(typeSpac)
                .build()
                .writeTo(mFiler)
            MSG.print("创建文件成功：${clazName}")
        }


    }

}