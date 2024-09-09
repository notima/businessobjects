package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;

import org.notima.generic.businessobjects.TimeRecord;

public interface TimeRecordService {

	public List<TimeRecord> getRecordsForUser(String userId, LocalDate fromDate, LocalDate untilDate);
	
	public List<TimeRecord> getRecordsForProject(String projectId, LocalDate fromDate, LocalDate untilDate);
	
}
