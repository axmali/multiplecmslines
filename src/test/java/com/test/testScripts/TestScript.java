package com.test.testScripts;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.test.Utils.Functions;
import com.test.Utils.getData;
import com.test.pages.BillEntryPage;
import com.test.pages.LoginPage;

/**
 * Unit test for simple App.
 */
public class TestScript 

{
	static WebDriver driver;
	static LoginPage loginPage;
	static BillEntryPage billEntryPage;
	String URL="http://52.37.60.147/decisions/Login.aspx";
	static String[][] data;


	@BeforeSuite
	public void setUp() throws IOException{
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		driver=new FirefoxDriver();
		loginPage = new LoginPage(driver);
		billEntryPage = new BillEntryPage(driver);
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();	
		//Reader Header data Excel tab
		//		data = getData.readExcel("TestData3", "CMS");


	}

	@AfterSuite
	public void tearDown(){
		//driver.quit();
	}


	@Test
	public void test1() throws IOException{
		//		navigateToUserPortal();
		//		navigateToBillEntryMainPage();
		for(int iter=1;iter<=2;iter++){
			//			fillValuesInBillEntryPage(iter);
			//			billEntryPage.clickOnNextButton();
			String relatedNumberFromCMS = getData.valueFromHeader("CMS","RelNO",iter);
			String type = getData.valueFromHeader("CMS","Type",iter);
			List<Integer> lineItemsRowsForCMS= getData.rowNumberHavingSameRelatedNumber("LineitemCMS", "RelNO", relatedNumberFromCMS);
			for(int counter=0; counter<lineItemsRowsForCMS.size();counter++){
				fillValuesInLineItemsPage(type,  lineItemsRowsForCMS.get(counter), counter);
			}

			//			waitTime();
			//			GetLineItemDataCMS(); 
			//			readLineItemCMSFrmExl(iter);
			//			fillLineItemCMSValues();
		}

	}


	private void fillValuesInBillEntryPage(int rowData) throws IOException {

		billEntryPage.inputTextIntoClient(getData.valueFromHeader("CMS","clientName",rowData));
		billEntryPage.inputTextIntoClaim(getData.valueFromHeader("CMS","claimNum",rowData));
		billEntryPage.inputTextIntoState(getData.valueFromHeader("CMS","St",rowData));
		billEntryPage.inputTextIntoPaycode(getData.valueFromHeader("CMS","PayCode",rowData));
		if(billEntryPage.isEmptyBillID()){
			billEntryPage.inputTextIntoBillID(getData.valueFromHeader("CMS","BillID",rowData));
		}
		billEntryPage.inputTextIntoBillTaxID(getData.valueFromHeader("CMS","BillingTaxID",rowData));
		Functions.pressTabKey(billEntryPage.billTaxIDElement());

		try{
			if(billEntryPage.isDisplayedWarningIcon()){
				billEntryPage.clickOnBillTaxIdLabel();
				billEntryPage.clickOnFirstRowSelectProvider();
				billEntryPage.clickOnSelectProviderButton();
			}
			else if(billEntryPage.isDisplayedCrossIcon()){
				throw new NoSuchElementException("Cross mark is dispalyed for Billing tax ID");
			}
			else{
				//				System.out.println("Valid Tax ID: "+Tax ID);
			}
		}
		catch(Exception e){
			System.out.println("warning icon is not visible for billing txn id");
		}


		billEntryPage.inputTextIntoBillNPI(getData.valueFromHeader("CMS","BillingNPI",rowData));
		billEntryPage.inputTextIntoRenderNPI(getData.valueFromHeader("CMS","renderNPI",rowData));
		System.out.println("zip: "+getData.valueFromHeader("CMS","ZipCode",rowData));
		billEntryPage.inputTextIntoLoczip(getData.valueFromHeader("CMS","ZipCode",rowData));
		billEntryPage.inputTextIntoFacilityNPI(getData.valueFromHeader("CMS","ProvFacNPI",rowData));
		billEntryPage.inputTextDateIntoDOSFrm(getData.valueFromHeader("CMS","DOSFrom",rowData));
		billEntryPage.inputTextIntoPatient(getData.valueFromHeader("CMS","PatientID",rowData));
		billEntryPage.inputTextDateIntoBillDate(getData.valueFromHeader("CMS","ProviderBillDate",rowData));
		billEntryPage.inputTextIntoProcedureCode(getData.valueFromHeader("CMS","ICDProcedureCode",rowData));
		billEntryPage.inputTextDateIntoClientRecvdDate(getData.valueFromHeader("CMS","ClientReceivedDate",rowData));
		billEntryPage.inputTextDateIntoBillRecvdDate(getData.valueFromHeader("CMS","BillReceivedDate",rowData));





		//  UB-04 fields
		String billTypeTxt=billEntryPage.getAttributeBillTypeText();

		if(billTypeTxt.equalsIgnoreCase("UB-04")){

			billEntryPage.inputTextIntoTypeofBill(getData.valueFromHeader("CMS","TypeofBill",rowData));
			billEntryPage.inputTextIntoMedicareNO(getData.valueFromHeader("CMS","MedicareNumber",rowData));

			billEntryPage.inputTextDateIntoADmDt(getData.valueFromHeader("CMS","AdmitDate",rowData));
			billEntryPage.inputTextDateIntoDschgDt(getData.valueFromHeader("CMS","DischargeDate",rowData));
			billEntryPage.inputTextIntoAdmsnHr(getData.valueFromHeader("CMS","AdmissionHour",rowData));
			billEntryPage.inputTextIntoAdmissionType(getData.valueFromHeader("CMS","AdmissionType",rowData));
			billEntryPage.inputTextIntoAdmissionSrc(getData.valueFromHeader("CMS","AdmissionSource",rowData));
			billEntryPage.inputTextIntoDischargeHr(getData.valueFromHeader("CMS","DischargeHour",rowData));
			billEntryPage.inputTextIntoDischrgStatus(getData.valueFromHeader("CMS","DischargeStatus",rowData));
			billEntryPage.inputTextIntoAdmittingDiag(getData.valueFromHeader("CMS","AdmittingDiagnosis",rowData));
			billEntryPage.inputTextIntoPrinProcCDs(getData.valueFromHeader("CMS","PrinProcCodes",rowData));
			billEntryPage.inputTextIntoPrinProcDt(getData.valueFromHeader("CMS","PrinProcDate",rowData));
			billEntryPage.inputTextIntoOpProvNPI(getData.valueFromHeader("CMS","OperationProvNPI",rowData));
			billEntryPage.inputTextIntoDrgcd(getData.valueFromHeader("CMS","DRG",rowData));


		}



	}


	private void fillValuesInLineItemsPage(String valueFromHeader, int rowData, int position) throws IOException {
		// TODO Auto-generated method stub
		billEntryPage.inputTextIntoPOS(getData.valueFromHeader("CMS","POS",rowData), position);
		billEntryPage.inputTextIntoTOS(getData.valueFromHeader("CMS","TOS",rowData), position);

		if(valueFromHeader.equalsIgnoreCase("CMS")){

			billEntryPage.inputTextIntoBillCd(getData.valueFromHeader("CMS","BillCode",rowData), position);
			billEntryPage.inputTextIntoBillMd(getData.valueFromHeader("CMS","BillMod",rowData), position);
			//billEntryPage.inputTextIntoRevCd(getData.valueFromHeader("CMS","ReviewedCd",rowData));
			//billEntryPage.inputTextIntoRevMd(getData.valueFromHeader("CMS","ReviewedMd",rowData));
			billEntryPage.inputTextIntoCMSDays(getData.valueFromHeader("CMS","Days",rowData), position);
			billEntryPage.inputTextIntoChrgs(getData.valueFromHeader("CMS","Charges",rowData), position);
			billEntryPage.inputTextIntoDiag(getData.valueFromHeader("CMS","Diag",rowData), position);
			billEntryPage.inputTextIntoTotalCharges(getData.valueFromHeader("CMS","TotalCharges",rowData));
		}
		else if(valueFromHeader.equalsIgnoreCase("UB-04")){

			billEntryPage.inputTextIntoBillCd(getData.valueFromHeader("CMS","BillCode",rowData), position);
			billEntryPage.inputTextIntoBillMd(getData.valueFromHeader("CMS","BillMod",rowData), position);
			//billEntryPage.inputTextIntoRevCd(getData.valueFromHeader("CMS","ReviewedCd",rowData));
			//billEntryPage.inputTextIntoRevMd(getData.valueFromHeader("CMS","ReviewedMd",rowData));
			billEntryPage.inputTextIntoUB04Days(getData.valueFromHeader("CMS","Days",rowData), position);
			billEntryPage.inputTextIntoChrgs(getData.valueFromHeader("CMS","Charges",rowData), position);
			billEntryPage.inputTextIntoDiag(getData.valueFromHeader("CMS","Diag",rowData), position);
			billEntryPage.inputTextIntoTotalCharges(getData.valueFromHeader("CMS","TotalCharges",rowData));
		}
		else if(valueFromHeader.equalsIgnoreCase("RX")){
			billEntryPage.inputTextIntoBillCd(getData.valueFromHeader("CMS","BillCode",rowData), position);
			billEntryPage.inputTextIntoBillMd(getData.valueFromHeader("CMS","BillMod",rowData), position);
			//billEntryPage.inputTextIntoRevCd(getData.valueFromHeader("CMS","ReviewedCd",rowData));
			//billEntryPage.inputTextIntoRevMd(getData.valueFromHeader("CMS","ReviewedMd",rowData));
			billEntryPage.inputTextIntoRXDaysSupply(getData.valueFromHeader("CMS","Days",rowData));
			billEntryPage.inputTextIntoChrgs(getData.valueFromHeader("CMS","Charges",rowData), position);
			billEntryPage.inputTextIntoDiag(getData.valueFromHeader("CMS","Diag",rowData), position);
			billEntryPage.inputTextIntoTotalCharges(getData.valueFromHeader("CMS","TotalCharges",rowData));

		}


		billEntryPage.clickOnCheckSumButton();


		billEntryPage.clickOnPriceButton();
		System.out.println("Pricing is Done......");            //, FeeScheduleDiscount is :" + ActFeeDisc);


		String ActFeeDisc = billEntryPage.getTextFromFeeScheduleDis();

		String ExpFeeSchDis = getData.valueFromHeader("LineitemCMS","ExpFeeSchDis",rowData);

		if(ActFeeDisc.equals(ExpFeeSchDis)){
			System.out.println("Actual Fee Schedule is Correct: " +ExpFeeSchDis+" , "+ActFeeDisc);
		}
		else {
			System.out.println("Acutal Fee Schedule is Incorrect: " +ExpFeeSchDis+" , "+ActFeeDisc);
		}
		getData.writeValueInExcelFile("LineitemCMS","ExpFeeSchDis",rowData,ActFeeDisc);

	}	



	public void navigateToUserPortal(){
		loginPage.inputTextIntoUserName("amalik@ekhealth.com");
		loginPage.inputTextIntoPassword("Summer!2017");
		loginPage.clickOnSubmitButton();
		loginPage.ClickOnUserPortalButton();
	}

	public void navigateToBillEntryMainPage(){
		driver.get("http://52.37.60.147/decisions/Primary/H/?FolderId=56b91dee-8a71-11e6-ace6-00155d0fe70e&pageName=Billing%20Entry%20Main%20Page");

	}
}
