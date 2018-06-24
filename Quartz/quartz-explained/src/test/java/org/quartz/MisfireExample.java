package org.quartz;

import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class MisfireExample {

	public void run() throws Exception {

		// 任务执行的时间 格式化
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		System.out.println("--------------- 初始化 -------------------");

		// 下一个第15秒
		Date startTime = nextGivenSecondDate(null, 15);

		// statefulJob1 每3s运行一次,但它会延迟10s
//		JobDetail job = newJob(StatefulDumbJob.class)
//				.withIdentity("statefulJob1", "group1")
//				.usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L) // 设置参数:睡眠时间10s
//				.build();
//		SimpleTrigger trigger = newTrigger()
//				.withIdentity("trigger1", "group1")
//				.startAt(startTime)
//				.withSchedule(
//						simpleSchedule().withIntervalInSeconds(3)
//								.repeatForever()).build();
//
//
//		Date ft = sched.scheduleJob(job, trigger);
//		System.out.println(job.getKey().getName() + " 将在: "
//				+ dateFormat.format(ft) + "  时运行.并且重复: "
//				+ trigger.getRepeatCount() + " 次, 每次间隔 "
//				+ trigger.getRepeatInterval() / 1000 + " 秒");

		// statefulJob2 将每3s运行一次 , 但它将延迟10s, 然后不断的迭代,
		// 与statefulJob1不同的是设置了错失触发后的调整策略
//		job = newJob(StatefulDumbJob.class)
//				.withIdentity("statefulJob2", "group1")
//				.usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L)// 设置参数:睡眠时间
//				// 10s
//				.build();
//		trigger = newTrigger()
//				.withIdentity("trigger2", "group1")
//				.startAt(startTime)
//				.withSchedule(
//						simpleSchedule().withIntervalInSeconds(3)
//								.repeatForever()
//								// 设置错失触发后的调度策略
//								.withMisfireHandlingInstructionIgnoreMisfires())
//				.build();
//
//		ft = sched.scheduleJob(job, trigger);
//		System.out.println(job.getKey().getName() + " 将在: "
//				+ dateFormat.format(ft) + "  时运行.并且重复: "
//				+ trigger.getRepeatCount() + " 次, 每次间隔 "
//				+ trigger.getRepeatInterval() / 1000 + " 秒");
//
//		System.out.println("------- 开始调度 (调用.start()方法) ----------------");


		TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
		JobDetail jobDetail = JobBuilder.newJob(JobMisFireTest.class)
				.withIdentity("aaa", "bbb").build();

		//[秒] [分] [小时] [日] [月] [周] [年]
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
				.cronSchedule("20 10 10 24 6 ?")
				.inTimeZone(timeZone).withMisfireHandlingInstructionIgnoreMisfires();
		//按新的cronExpression表达式构建一个新的trigger
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("name1",
				"gropu").withSchedule(scheduleBuilder).build();

   // [秒] [分] [小时] [日] [月] [周] [年]
		Date ft = sched.scheduleJob(jobDetail, trigger);
		System.out.println(jobDetail.getKey().getName() + " 将在: "
				+ dateFormat.format(ft) + "  时运行.并且重复: ");

		sched.start();

		// 给任务留时间运行 600S
		Thread.sleep(600L * 1000L);

		sched.shutdown(true);
		System.out.println("------- 调度已关闭 ---------------------");

	}



	public static void main(String[] args) throws Exception {
		MisfireExample misfireExample = new MisfireExample();
		misfireExample.run();
	}
}
