package org.quartz;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;

import junit.framework.TestCase;

import java.util.TimeZone;

/**
 * DefaultSchedulerTest
 */
public class DefaultSchedulerTest extends TestCase {

    public void testAddJobNoTrigger() throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail = JobBuilder.newJob(JobImpTest.class)
                .withIdentity("aaa", "bbb").build();

        TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");

        //表达式调度构建器

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("10 * * * * ?")
                .inTimeZone(timeZone).withMisfireHandlingInstructionIgnoreMisfires();


        //按新的cronExpression表达式构建一个新的trigger
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("name1",
               "gropu").withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            assertThat(e.getMessage(), containsString("durable"));
        }
        scheduler.start();
        Thread.sleep(240L*1000);
    }
}

 class JobImpTest implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
       System.out.println("execute......");
    }

}