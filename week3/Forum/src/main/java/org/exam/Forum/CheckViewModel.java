package org.exam.Forum;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Window;

public class CheckViewModel {

	String value;

	@Init
	public void init(@BindingParam("param1") String param1) {
		value = param1;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Command
	public void close(@ContextParam(ContextType.VIEW) Window comp) {
		comp.detach();
	}
}
