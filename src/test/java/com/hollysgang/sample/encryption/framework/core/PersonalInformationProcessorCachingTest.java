package com.hollysgang.sample.encryption.framework.core;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalInformationProcessorCachingTest {

    private static PITargetHolder piTargetHolder;

    @BeforeAll
    static void init(){
        piTargetHolder = new PITargetHolder();
        piTargetHolder.setPITarget(TestDto.class);
        piTargetHolder.setPITarget(OuterTestDto.class);
        piTargetHolder.setPITarget(OuterTestInnerListDto.class);
        piTargetHolder.setPITarget(OuterTestInnerArrayDtos.class);
    }

    @Test
    void encryptPersonalInformation_DTO() {
        // given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setEncFunc("encTarget", (el) -> "encrypted:" + el);
        TestDto dto = new TestDto();
        dto.setEncryptField("I am Ironman");
        dto.setNoEncryptField("I am Billionaire");

        // execute
        pip.encryptPersonalInformation(dto);

        // result
        assertEquals("encrypted:I am Ironman", dto.getEncryptField());
        assertEquals("I am Billionaire", dto.getNoEncryptField());
    }

    @Test
    void encryptPersonalInformation_ListOfDTO() {
        // given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setEncFunc("encTarget", (el) -> "encrypted:" + el);
        TestDto dto = new TestDto();
        dto.setEncryptField("I am Ironman");
        TestDto dto2 = new TestDto();
        dto2.setEncryptField("I am Spiderman");
        TestDto dto3 = new TestDto();
        dto3.setEncryptField("I am Duck");
        List<TestDto> dtos = List.of(dto, dto2, dto3);

        // execute
        pip.encryptPersonalInformation(dtos);

        // result
        assertEquals("encrypted:I am Ironman", dtos.get(0).getEncryptField());
        assertEquals("encrypted:I am Spiderman", dtos.get(1).getEncryptField());
        assertEquals("encrypted:I am Duck", dtos.get(2).getEncryptField());
    }

    @Test
    void encryptPersonalInformation_DTOinDTO() {
        // given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setEncFunc("encTarget", (el) -> "encrypted:" + el);
        TestDto innerDto = new TestDto();
        innerDto.setEncryptField("I am Ironman");
        innerDto.setNoEncryptField("I am Billionaire");

        OuterTestDto outerDto = new OuterTestDto();
        outerDto.setInnerDto(innerDto);
        outerDto.setEncryptField("I am your father");
        outerDto.setNoEncryptField("I am your mother");
        // execute
        pip.encryptPersonalInformation(outerDto);

        // result
        assertEquals("encrypted:I am your father", outerDto.getEncryptField());
        assertEquals("I am your mother", outerDto.getNoEncryptField());
        assertEquals("encrypted:I am Ironman", outerDto.getInnerDto().getEncryptField());
        assertEquals("I am Billionaire", outerDto.getInnerDto().getNoEncryptField());
    }

    @Test
    void encryptPersonalInformation_ListOfDTOinDTO() {
        // given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setEncFunc("encTarget", (el) -> "encrypted:" + el);

        TestDto dto = new TestDto();
        dto.setEncryptField("I am Ironman");
        TestDto dto2 = new TestDto();
        dto2.setEncryptField("I am Spiderman");
        TestDto dto3 = new TestDto();
        dto3.setEncryptField("I am Duck");
        List<TestDto> innerDtos = List.of(dto, dto2, dto3);

        OuterTestInnerListDto outerDto = new OuterTestInnerListDto();
        outerDto.setInnerDtos(innerDtos);
        outerDto.setEncryptField("I am your father");
        outerDto.setNoEncryptField("I am your mother");

        // execute
        pip.encryptPersonalInformation(outerDto);

        // result
        assertEquals("encrypted:I am your father", outerDto.getEncryptField());
        assertEquals("I am your mother", outerDto.getNoEncryptField());
        assertEquals("encrypted:I am Ironman", outerDto.getInnerDtos().get(0).getEncryptField());
        assertEquals("encrypted:I am Spiderman", outerDto.getInnerDtos().get(1).getEncryptField());
        assertEquals("encrypted:I am Duck", outerDto.getInnerDtos().get(2).getEncryptField());
    }

    @Test
    void encryptPersonalInformation_ArrayOfDTOinDTO() {
        // given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setEncFunc("encTarget", (el) -> "encrypted:" + el);

        TestDto dto = new TestDto();
        dto.setEncryptField("I am Ironman");
        TestDto dto2 = new TestDto();
        dto2.setEncryptField("I am Spiderman");
        TestDto dto3 = new TestDto();
        dto3.setEncryptField("I am Duck");
        TestDto[] innerDtos = new TestDto[]{dto, dto2, dto3};

        OuterTestInnerArrayDtos outerDto = new OuterTestInnerArrayDtos();
        outerDto.setInnerDtos(innerDtos);
        outerDto.setEncryptField("I am your father");
        outerDto.setNoEncryptField("I am your mother");

        // execute
        pip.encryptPersonalInformation(outerDto);

        // result
        assertEquals("encrypted:I am your father", outerDto.getEncryptField());
        assertEquals("I am your mother", outerDto.getNoEncryptField());
        assertEquals("encrypted:I am Ironman", outerDto.getInnerDtos()[0].getEncryptField());
        assertEquals("encrypted:I am Spiderman", outerDto.getInnerDtos()[1].getEncryptField());
        assertEquals("encrypted:I am Duck", outerDto.getInnerDtos()[2].getEncryptField());
    }

    @Test
    void decryptPersonalInformation_DTO() {
        //given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setDecFunc("encTarget", (el) -> el.replace("encrypted:", ""));
        TestDto dto = new TestDto();
        dto.setEncryptField("encrypted:I am Ironman");
        dto.setNoEncryptField("unencrypted: I am your father");

        //execute
        pip.decryptPersonalInformation(dto);

        // expect
        assertEquals("I am Ironman", dto.getEncryptField());
        assertEquals("unencrypted: I am your father", dto.getNoEncryptField());
    }

    @Test
    void decryptPersonalInformation_ListOfDto() {
        //given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setDecFunc("encTarget", (el) -> el.replace("encrypted:", ""));
        TestDto dto = new TestDto();
        dto.setEncryptField("encrypted:I am Ironman");
        TestDto dto2 = new TestDto();
        dto2.setEncryptField("encrypted:I am NewJeans");
        TestDto dto3 = new TestDto();
        dto3.setEncryptField("encrypted:I am APT");

        List<TestDto> dtos = List.of(dto, dto2, dto3);
        //execute
        pip.decryptPersonalInformation(dtos);

        // expect
        assertEquals("I am Ironman", dtos.get(0).getEncryptField());
        assertEquals("I am NewJeans", dtos.get(1).getEncryptField());
        assertEquals("I am APT", dtos.get(2).getEncryptField());
    }

    @Test
    void decryptPersonalInformation_DTOinDTO() {
        //given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setDecFunc("encTarget", (el) -> el.replace("encrypted:", ""));
        TestDto innerDto = new TestDto();
        innerDto.setEncryptField("encrypted:I am Ironman");
        innerDto.setNoEncryptField("unencrypted: I am your father");
        OuterTestDto outerDto = new OuterTestDto();
        outerDto.setInnerDto(innerDto);
        outerDto.setEncryptField("encrypted:I am Rabbit");
        outerDto.setNoEncryptField("unencrypted:I am Korean");
        outerDto.setInnerDto(innerDto);

        //execute
        pip.decryptPersonalInformation(outerDto);

        // expect
        assertEquals("I am Ironman", outerDto.getInnerDto().getEncryptField());
        assertEquals("unencrypted: I am your father", outerDto.getInnerDto().getNoEncryptField());
        assertEquals("I am Rabbit", outerDto.getEncryptField());
        assertEquals("unencrypted:I am Korean", outerDto.getNoEncryptField());
    }


    @Test
    void decryptPersonalInformation_ListOfDTOinDTO() {
        // given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setDecFunc("encTarget", (el) -> el.replace("encrypted:", ""));

        TestDto dto = new TestDto();
        dto.setEncryptField("encrypted:I am Ironman");
        TestDto dto2 = new TestDto();
        dto2.setEncryptField("encrypted:I am NewJeans");
        TestDto dto3 = new TestDto();
        dto3.setEncryptField("encrypted:I am APT");
        List<TestDto> innerDtos = List.of(dto, dto2, dto3);

        OuterTestInnerListDto outerDto = new OuterTestInnerListDto();
        outerDto.setInnerDtos(innerDtos);
        outerDto.setEncryptField("encrypted:I am your father");
        outerDto.setNoEncryptField("I am your mother");

        // execute
        pip.decryptPersonalInformation(outerDto);

        // result
        assertEquals("I am your father", outerDto.getEncryptField());
        assertEquals("I am your mother", outerDto.getNoEncryptField());
        assertEquals("I am Ironman", outerDto.getInnerDtos().get(0).getEncryptField());
        assertEquals("I am NewJeans", outerDto.getInnerDtos().get(1).getEncryptField());
        assertEquals("I am APT", outerDto.getInnerDtos().get(2).getEncryptField());
    }

    @Test
    void decryptPersonalInformation_ArrayOfDTOinDTO() {
        // given
        PersonalInformationProcessorCaching pip = new PersonalInformationProcessorCaching(piTargetHolder);
        pip.setDecFunc("encTarget", (el) -> el.replace("encrypted:", ""));

        TestDto dto = new TestDto();
        dto.setEncryptField("encrypted:I am Ironman");
        TestDto dto2 = new TestDto();
        dto2.setEncryptField("encrypted:I am NewJeans");
        TestDto dto3 = new TestDto();
        dto3.setEncryptField("encrypted:I am APT");
        TestDto[] innerDtos = new TestDto[]{dto, dto2, dto3};

        OuterTestInnerArrayDtos outerDto = new OuterTestInnerArrayDtos();
        outerDto.setInnerDtos(innerDtos);
        outerDto.setEncryptField("encrypted:I am your father");
        outerDto.setNoEncryptField("I am your mother");

        // execute
        pip.decryptPersonalInformation(outerDto);

        // result
        assertEquals("I am your father", outerDto.getEncryptField());
        assertEquals("I am your mother", outerDto.getNoEncryptField());
        assertEquals("I am Ironman", outerDto.getInnerDtos()[0].getEncryptField());
        assertEquals("I am NewJeans", outerDto.getInnerDtos()[1].getEncryptField());
        assertEquals("I am APT", outerDto.getInnerDtos()[2].getEncryptField());
    }



    @Getter
    @Setter
    private static class TestDto extends AbstractDto {
        @PersonalInformation("encTarget")
        private String encryptField;
        private String noEncryptField;
    }

    @Getter
    @Setter
    private static class OuterTestDto extends AbstractDto {
        private TestDto innerDto;
        @PersonalInformation("encTarget")
        private String encryptField;
        private String noEncryptField;
    }

    @Getter
    @Setter
    private static class OuterTestInnerListDto extends AbstractDto {
        private List<TestDto> innerDtos;
        @PersonalInformation("encTarget")
        private String encryptField;
        private String noEncryptField;
    }

    @Getter
    @Setter
    private static class OuterTestInnerArrayDtos extends AbstractDto {
        private TestDto[] innerDtos;
        @PersonalInformation("encTarget")
        private String encryptField;
        private String noEncryptField;
    }
}