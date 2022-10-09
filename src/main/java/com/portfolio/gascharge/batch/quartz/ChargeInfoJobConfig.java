package com.portfolio.gascharge.batch.quartz;

import com.portfolio.gascharge.batch.api.ChargeApi;
import com.portfolio.gascharge.batch.api.ChargeApiDto;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ChargeInfoJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ChargeApi chargeApi;
    private final ChargeRepository chargeRepository;
    private final EntityManagerFactory emf;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("saveChargeDataFromApiJob")
                .incrementer(new RunIdIncrementer())
                .start(saveChargeDataFromApi())
                .next(changeMembershipTest())
                .build();
    }

    @Bean
    public Step saveChargeDataFromApi() {
        return this.stepBuilderFactory.get("saveChargeDataFromApiStep")
                .<ChargeApiDto, Charge>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @JobScope
    public ListItemReader<ChargeApiDto> reader() {
        List<ChargeApiDto> list = null;

        try {
            ResponseEntity<String> chargeJsonData = chargeApi.getChargeResponseEntity();

            list = chargeApi.getChargeDtoList(chargeJsonData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ListItemReader<>(list);
    }

    @Bean
    public ItemProcessor<ChargeApiDto, Charge> processor() {
        return item -> {
            log.info(" 배치 프로세스 진행중...");

            Optional<Charge> byId = chargeRepository.findById(item.getId());

            if (byId.isEmpty()) {
                return item.toEntity();
            } else {
                Charge charge = byId.get();
                charge.update(item.getTotalCount(), item.getCurrentCount());
                return charge;
            }
        };
    }

    @Bean
    public JpaItemWriter<Charge> writer() {
        JpaItemWriter<Charge> build = new JpaItemWriterBuilder<Charge>()
                .entityManagerFactory(emf)
                .usePersist(true)
                .build();

        return build;
    }

    @Bean
    public Step changeMembershipTest() {
        return this.stepBuilderFactory.get("changeMembershipTest step")
                .tasklet((contribution, chunkContext) -> {
                    chargeRepository.findByName("오곡").forEach(e -> e.setMembership(ChargePlaceMembership.MEMBERSHIP));
                    chargeRepository.findByName("국회").forEach(e -> e.setMembership(ChargePlaceMembership.MEMBERSHIP));
                    return RepeatStatus.FINISHED;
               }).build();
    }
}
