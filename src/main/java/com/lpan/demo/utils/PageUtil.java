package com.lpan.demo.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.util.WebUtils;

public class PageUtil {
	
	/**
	 * 分页参数转换Pageable
	 * @param request
	 * @return
	 */
	public static Pageable covertPageable(HttpServletRequest request) {
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "");
		Integer pageNumber = Integer.valueOf((String) params.get("pageNumber"))-1;
		Integer pageSize = Integer.valueOf((String) params.get("pageSize"));
		String sortOrder = ((String) params.get("sortOrder")).toUpperCase();
		String sortName = (String) params.get("sortName");
		Sort sort = Sort.by(Direction.valueOf(sortOrder), new String[] {sortName});
		return PageRequest.of(pageNumber, pageSize, sort);
		
	}

}
