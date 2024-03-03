package com.shope.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shope.common.entity.Setting;
import com.shope.common.entity.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
	
	@Autowired SettingRepository repo;
	
	@Test
	public void testCreateGeneralSetting() {
		//Setting siteName = new Setting("SITE_NAME","ShopE",SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO","ShopE.png",SettingCategory.GENERAL);
		Setting copyright = new Setting("COPPYRIGHT","Copyright (c) 2021 Shope Ltd.",SettingCategory.GENERAL);
		repo.saveAll(List.of(siteLogo,copyright));
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySetting() {
		Setting currencyId = new Setting("CURRENCY_ID","1",SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL","$",SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION","before",SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE","POINT",SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS","2",SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE","COMMA",SettingCategory.CURRENCY);
		
		repo.saveAll(List.of(currencyId,symbol,symbolPosition,decimalPointType,decimalDigits,thousandsPointType));
		repo.save(decimalPointType);
	}
	
	@Test
	public void testListSettingByCategory() {
		List<Setting> settings =repo.findByCategory(SettingCategory.GENERAL);
		
		settings.forEach(System.out::println);
	}
}
