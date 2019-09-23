//package local.project.Inzynierka;
//
//
//import local.project.Inzynierka.persistence.entity.Branch;
//import local.project.Inzynierka.persistence.entity.Category;
//import local.project.Inzynierka.persistence.entity.Company;
//import local.project.Inzynierka.persistence.entity.Voivoideship;
//import local.project.Inzynierka.servicelayer.dto.AddCompanyDto;
//import local.project.Inzynierka.servicelayer.dto.Address;
//import local.project.Inzynierka.servicelayer.dto.CompanyBranchDto;
//import local.project.Inzynierka.servicelayer.dto.Voivodeship;
//import local.project.Inzynierka.servicelayer.dto.mapper.CompanyExtractor;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//@RunWith(Parameterized.class)
//public class CompanyExtractorTest {
//
//    private AddCompanyDto input;
//
//    private Company expectedCompany;
//    private List<Branch> expectedBranches;
//
//    public CompanyExtractorTest(AddCompanyDto input, Company expectedCompany, List<Branch> expectedBranches) {
//        this.input = input;
//        this.expectedCompany = expectedCompany;
//        this.expectedBranches = expectedBranches;
//    }
//
//    @Parameterized.Parameters
//    public static Collection input() {
//        return Arrays.asList(new Object[][]{
//                {
//                        buildActualAddCompanyDtoWithoutBranches(),
//                        buildExpectedCompanyWithoutBranches(),
//                        null
//                },
//                {
//                        buildActualAddCompanyDtoWithSingleBranch(),
//                        buildExpectedCompanyWithSingleBranch(),
//                        buildExpectedBranchList()
//                },
//
//                });
//    }
//
//    private static AddCompanyDto buildActualAddCompanyDtoWithoutBranches() {
//        return AddCompanyDto.builder()
//                .address(new Address(Voivodeship.LUBELSKIE, "Lublin", "Lubelska", "5", "54"))
//                .branches(null)
//                .category("Motoryzacja")
//                .name("Firma 1")
//                .NIP("12345678901234")
//                .REGON("1234567890")
//                .description("Jakis opis").build();
//    }
//
//    private static Company buildExpectedCompanyWithoutBranches() {
//        return Company.builder()
//                .address(local.project.Inzynierka.persistence.entity.Address.builder()
//                                 .voivodeship_id(new Voivoideship("lubelskie"))
//                                 .city("Lublin")
//                                 .street("Lubelska")
//                                 .buildingNo("5")
//                                 .apartmentNo("54")
//                                 .build())
//                .category(new Category("Motoryzacja"))
//                .description("Jakis opis")
//                .name("Firma 1")
//                .NIP("12345678901234")
//                .REGON("1234567890")
//                .hasBranch(false)
//                .build();
//    }
//
//    private static AddCompanyDto buildActualAddCompanyDtoWithSingleBranch() {
//        return AddCompanyDto.builder()
//                .address(new Address(Voivodeship.LUBELSKIE, "Lublin", "Lubelska", "5", "23"))
//                .branches(Arrays.asList(
//                        CompanyBranchDto.builder()
//                                .address(new Address(Voivodeship.LUBELSKIE, "Lublin", "Polska", "12", "11"))
//                                .geoX(54.3F)
//                                .geoY(52.3F)
//                                .name("Firma 1 zaklad1")
//                                .build()
//                ))
//                .category("Motoryzacja")
//                .name("Firma 1")
//                .NIP("12345678901234")
//                .REGON("1234567890")
//                .description("Jakis opis").build();
//    }
//
//    private static Company buildExpectedCompanyWithSingleBranch() {
//        return Company.builder()
//                .address(local.project.Inzynierka.persistence.entity.Address.builder()
//                                 .voivodeship_id(new Voivoideship("lubelskie"))
//                                 .city("Lublin")
//                                 .street("Lubelska")
//                                 .buildingNo("5")
//                                 .apartmentNo("23")
//                                 .build())
//                .category(new Category("Motoryzacja"))
//                .description("Jakis opis")
//                .name("Firma 1")
//                .NIP("12345678901234")
//                .REGON("1234567890")
//                .hasBranch(true)
//                .build();
//    }
//
//    private static List<Branch> buildExpectedBranchList() {
//        return Arrays.asList(
//                Branch.builder()
//                        .address(local.project.Inzynierka.persistence.entity.Address.builder()
//                                         .voivodeship_id(new Voivoideship("lubelskie"))
//                                         .city("Lublin")
//                                         .street("Polska")
//                                         .buildingNo("12")
//                                         .apartmentNo("11")
//                                         .build())
//                        .company(buildExpectedCompanyWithSingleBranch())
//                        .geoX(54.3F)
//                        .geoY(52.3F)
//                        .name("Firma 1 zaklad1")
//                        .build()
//
//        );
//    }
//
//    @Test
//    public void testReturnCreatedCompany() {
//        Assert.assertEquals(expectedCompany, new CompanyExtractor(input).getCompany());
//    }
//
//    @Test
//    public void testReturnCreatedBranches() {
//        Assert.assertEquals(expectedBranches, new CompanyExtractor(input).getBranches());
//    }
//
//}
