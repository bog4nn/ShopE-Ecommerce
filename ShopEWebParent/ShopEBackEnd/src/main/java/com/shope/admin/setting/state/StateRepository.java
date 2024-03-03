package com.shope.admin.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shope.common.entity.Country;
import com.shope.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);
}
