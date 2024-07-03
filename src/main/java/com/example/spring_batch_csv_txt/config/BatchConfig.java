package com.example.spring_batch_csv_txt.config;

import com.example.spring_batch_csv_txt.model.MyCsvRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<MyCsvRecord, MyCsvRecord>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public FlatFileItemReader<MyCsvRecord> reader() {
        FlatFileItemReader<MyCsvRecord> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("src/main/resources/inputfile.csv"));
        reader.setLineMapper(new DefaultLineMapper<MyCsvRecord>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"field1", "field2", "field3"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<MyCsvRecord>() {{
                setTargetType(MyCsvRecord.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<MyCsvRecord, MyCsvRecord> processor() {
        return new ItemProcessor<MyCsvRecord, MyCsvRecord>() {
            @Override
            public MyCsvRecord process(MyCsvRecord item) throws Exception {
                // Process the data if needed
                return item;
            }
        };
    }

    @Bean
    public FlatFileItemWriter<MyCsvRecord> writer() {
        FlatFileItemWriter<MyCsvRecord> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output_file.txt"));
        writer.setLineAggregator(new LineAggregator<MyCsvRecord>() {
            @Override
            public String aggregate(MyCsvRecord item) {
                // Customize how the item is written to the file
                return item.toString();
            }
        });
        return writer;
    }


}
