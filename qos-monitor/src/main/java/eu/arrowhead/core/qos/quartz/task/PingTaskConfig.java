package eu.arrowhead.core.qos.quartz.task;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.CoreCommonConstants;
import eu.arrowhead.common.quartz.AutoWiringSpringBeanQuartzTaskFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class PingTaskConfig {

	//=================================================================================================
	// members

	protected Logger logger = LogManager.getLogger(PingTaskConfig.class);

	@Autowired
	private ApplicationContext applicationContext; //NOSONAR

	@Value(CoreCommonConstants.$PING_TTL_INTERVAL_WD)
	private int ttlInterval;

	private static final int SCHEDULER_DELAY = 1;

	private static final String NAME_OF_TRIGGER = "Intra_Cloud_Ping_Task_Trigger";
	private static final String NAME_OF_TASK = "Intra_Cloud_Ping_Task_Detail";	

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Bean
	public SchedulerFactoryBean pingSheduler() {
		final AutoWiringSpringBeanQuartzTaskFactory jobFactory = new AutoWiringSpringBeanQuartzTaskFactory();
		jobFactory.setApplicationContext(applicationContext);

		final SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		schedulerFactory.setJobFactory(jobFactory);
		schedulerFactory.setJobDetails(intraCloudPingTaskDetails().getObject());
		schedulerFactory.setTriggers(intraCloudPingTaskTrigger().getObject());
		schedulerFactory.setStartupDelay(SCHEDULER_DELAY);
		logger.info("Ping task adjusted with ttl interval: {} minutes", ttlInterval);

		return schedulerFactory;
	}

	//-------------------------------------------------------------------------------------------------
	@Bean
	public SimpleTriggerFactoryBean intraCloudPingTaskTrigger() {
		final SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
		trigger.setJobDetail(intraCloudPingTaskDetails().getObject());
		trigger.setRepeatInterval(ttlInterval * CommonConstants.CONVERSION_MILLISECOND_TO_MINUTE);
		trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		trigger.setName(NAME_OF_TRIGGER);

		return trigger;
	}

	//-------------------------------------------------------------------------------------------------
	@Bean
    public JobDetailFactoryBean intraCloudPingTaskDetails() {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PingTask.class);
		jobDetailFactory.setName(NAME_OF_TASK);
		jobDetailFactory.setDurability(true);

		return jobDetailFactory;
	}
}