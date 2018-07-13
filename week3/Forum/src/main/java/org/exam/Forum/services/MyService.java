package org.exam.Forum.services;

import org.exam.Forum.entity.Log;
import java.util.List;

public interface MyService {

	Log addLog(Log log);

	List<Log> getLogs();

	void deleteLog(Log log);
}
