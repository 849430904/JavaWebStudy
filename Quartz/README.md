
* [核心源码解释](quartz-explained)
* [quartz demo演练](https://github.com/yangshangwei/SpringMaster)

 ````
   需要安装mavean
   需要安装pom.xml里面的jar包
 ````
 
*  [quartz 漏触发](https://blog.csdn.net/yangshangwei/article/details/78539433#withmisfirehandlinginstructiondonothing)

* 群集的时候丢失任务原因？
	* 服务器线程数量不够用
	* 任务并发量太大（添加任务的时候任务密度不能太大或太集中）
	* 对业务数据库进行优化，执行任务的时候杜绝一些非常消耗数据库CPU或高性能的操作

* [执行Jobj时发生了错误怎么办？](https://blog.csdn.net/caomiao2006/article/details/46417199)

	* 立即重新执行任务
	* 立即停止所有相关这个任务的触发器 
  
````

比如以下代码，如何出错，休息一会让调度器马上再试一次
public class JobMisFireTest implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("ecute........."+context.getRefireCount());


        try {
            Thread.sleep(1000);
        }catch (Exception e){

        }

//        StackTraceElement[] stackTraceElements =  Thread.currentThread().getStackTrace();
//        for (int i=0;i<stackTraceElements.length;i++){
//            System.out.println(stackTraceElements[i].toString());
//        }

        if(context.getRefireCount() < 1) {//重试一次
            JobExecutionException jobExecutionException = new JobExecutionException();
            jobExecutionException.setRefireImmediately(true);
            throw jobExecutionException;
        }
    }
}
````