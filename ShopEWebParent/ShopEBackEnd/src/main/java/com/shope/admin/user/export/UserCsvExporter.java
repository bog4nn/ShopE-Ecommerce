package com.shope.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shope.admin.user.AbstractExporter;
import com.shope.common.entity.User;

public class UserCsvExporter extends AbstractExporter {
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response,"text/csv", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
		
		String[] csvHeader = {"User ID","E-mail","First Name","Last Name","Roles","Enabled"};
		String[] fieldMapping = {"id","email","firstName","lastNamme","roles","enabled"};
		
		
		csvWriter.writeHeader(csvHeader);
		
		for(User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}
		
		csvWriter.close();
	}
}
