package org.quartz;

/**
 * Created by zhangping on 2018/6/24.
 */
public class JobMisFireTest implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("ecute.........");


        try {
            Thread.sleep(1000);
        }catch (Exception e){

        }

//        StackTraceElement[] stackTraceElements =  Thread.currentThread().getStackTrace();
//        for (int i=0;i<stackTraceElements.length;i++){
//            System.out.println(stackTraceElements[i].toString());
//        }

        if(context.getRefireCount() < 2) {//重试一次
            JobExecutionException jobExecutionException = new JobExecutionException();
            jobExecutionException.setRefireImmediately(true);
            throw jobExecutionException;
        }
    }
}
