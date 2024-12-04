package com.hollysgang.sample.encryption.framework.core;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ReflectionBenchmarkTest {

    PersonalInformationProcessorReflection pir;
    PersonalInformationProcessorCaching pic;
    String piType = "rrn";
    List<TestDto> dtos;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ReflectionBenchmarkTest.class.getSimpleName())
                .forks(3)
                .warmupIterations(1)
                .measurementIterations(2)
                .build();
        new Runner(opt).run();
    }

    @Setup(Level.Trial)
    public void setup() {
        System.out.println("setup for benchmark test");

        PersonalInformationProcessorReflection pir = new PersonalInformationProcessorReflection();
        pir.setEncFunc(piType, (t) -> t);
        pir.setDecFunc(piType, (t) -> t);
        this.pir = pir;

        PITargetHolder pih = new PITargetHolder();
        pih.setPITarget(TestDto.class);
        PersonalInformationProcessorCaching pic = new PersonalInformationProcessorCaching(pih);
        pic.setEncFunc(piType, (t) -> t);
        pic.setDecFunc(piType, (t) -> t);
        this.pic = pic;

        dtos = new ArrayList<>();
        for(int i = 0; i < 1000; i += 1) {
            TestDto dto = new TestDto();
            dto.setRrn(Integer.toString(i));
            dtos.add(dto);
        }
    }

    @Benchmark
    public void pirBenchmark() {
        for(TestDto dto : dtos){
            pir.encryptPersonalInformation(dto);
            pir.decryptPersonalInformation(dto);
        }
    }

    @Benchmark
    public void picBenchmark() {
        for(TestDto dto : dtos){
            pic.encryptPersonalInformation(dto);
            pic.decryptPersonalInformation(dto);
        }
    }

    public static class TestDto extends AbstractDto {
        @PersonalInformation("rrn")
        private String rrn;
        private String prop1;
        private String prop2;
        private String prop3;
        private String prop4;
        private String prop5;
        private String prop6;
        private String prop7;


        public String getRrn(){
            return rrn;
        }

        public void setRrn(String rrn){
            this.rrn = rrn;
        }
    }

}
