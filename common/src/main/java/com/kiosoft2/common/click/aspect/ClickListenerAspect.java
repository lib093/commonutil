package com.kiosoft2.common.click.aspect;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ClickListenerAspect {

    public static ClickListenerAspect aspectOf() {
        return new ClickListenerAspect();
    }
//
////    @Around("execution(* android.view.View.OnClickListener.onClick(..)) ")
////    public void aroundClick(ProceedingJoinPoint joinPoint) {
//@Around("execution(* *..*lambda*(..))")
//public void interceptOnClick(ProceedingJoinPoint joinPoint, View view) {
//        try {
//        Method method = getMethod(joinPoint);
//        if(method == null){
//            Log.d("lance", "method == null");
//            if (!ClickUtil.INSTANCE.isFastDoubleClick()){
//                Log.d("lance", "执行点击事件");
//                joinPoint.proceed();
//            }else{
//                Log.d("lance", "刚点击过不在执行");
//            }
//            return;
//        }
//        Annotation[] annotations = method.getAnnotations();
//        boolean isFilter = true;
//        if (annotations != null && annotations.length > 0) {
//            for (int index = 0;index < annotations.length;index++){
//                Annotation annotation = annotations[index];
//                if (annotation instanceof ClickFilter) {
//                    isFilter = false;
//                    break;
//                }
//            }
//            if (isFilter){
//                if (!ClickUtil.INSTANCE.isFastDoubleClick()){
//                    Log.d("lance", "执行点击事件");
//                        joinPoint.proceed();
//                }else{
//                    Log.d("lance", "刚点击过不在执行");
//                }
//            }else{
//                Log.d("lance", "不拦截直接执行点击事件");
//                joinPoint.proceed();
//            }
//        }else{
//            if (!ClickUtil.INSTANCE.isFastDoubleClick()){
//                Log.d("lance", "执行点击事件");
//                joinPoint.proceed();
//            }else{
//                Log.d("lance", "刚点击过不在执行");
//            }
//        }
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
//    }
//   private Method getMethod(ProceedingJoinPoint joinPoint){
//       Signature signature = joinPoint.getSignature();
//       if (signature instanceof MethodSignature){
//           Log.d("lance", "MethodSignature");
//           Log.d("lance", "signature 1 "+signature.getName());
//           MethodSignature methodSignature = (MethodSignature)signature;
//           return methodSignature.getMethod();
//       }else {
//           Log.d("lance", "signature  "+signature.getName());
//           return  null;
//       }
//
//    }
}
