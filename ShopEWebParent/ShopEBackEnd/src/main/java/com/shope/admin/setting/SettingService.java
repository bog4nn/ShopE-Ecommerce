package com.shope.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shope.common.entity.Setting;
import com.shope.common.entity.SettingCategory;

@Service
public class SettingService {
	@Autowired private SettingRepository repo;
	
	public List<Setting> listAllSetting(){
		return (List<Setting>) repo.findAll();
	}
	
	public GeneralSettingBag getGeneralSetting() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySetting = repo.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySetting);
		
		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}
}
