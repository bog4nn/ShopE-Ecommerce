package com.shope.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shope.common.entity.Setting;
import com.shope.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	public List<Setting> findByCategory(SettingCategory category);
}
