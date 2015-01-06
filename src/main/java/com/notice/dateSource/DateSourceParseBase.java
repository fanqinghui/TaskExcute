package com.notice.dateSource;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.notice.work.ReportTemplateService;

public class DateSourceParseBase {
	@Resource
	protected ReportTemplateService reportTemplateService;
}
