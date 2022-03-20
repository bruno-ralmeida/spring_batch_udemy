package com.xxnbr.firstprojectbatch;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableBatchProcessing
public class EvenOrOddBatchConfig {

	@Autowired
	private JobBuilderFactory jobFactory;
	
	@Autowired
	private StepBuilderFactory stepFactory;
	
	@Bean
	public Job printEvenOrOddJob() {
		return jobFactory.get("printEvenOrOddJob")
				.start(printEvenOrOddStep())
				.incrementer(new RunIdIncrementer())
				.build();
				
	}
	
	public Step printEvenOrOddStep() {
		return stepFactory
				.get("printEvenOrOddStep")
				.<Integer,String>chunk(100) //Como estamos utilizando chunck, devemos informar o que será lido e escrito
				.reader(counterToTenReader()) //É obrigatório incluir um leitor e um escritor, processador é opcional
				.processor(evenOrOddProcessor())
				.writer(printWriter())
				.build();
	}
	
	public IteratorItemReader<Integer> counterToTenReader() {
		List<Integer> numberFromOneToTen = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		return new IteratorItemReader<Integer>(numberFromOneToTen.iterator());
	}
	
	
	public FunctionItemProcessor<Integer, String> evenOrOddProcessor(){
		
		return new FunctionItemProcessor<Integer, String>(item -> item % 2 == 0 ? String.format("Item %s é par", item): String.format("Item %s é impar", item));
		
	}
	
	public ItemWriter<String>printWriter() {
		return itens -> itens.forEach(System.out::println);
	}
	
}
