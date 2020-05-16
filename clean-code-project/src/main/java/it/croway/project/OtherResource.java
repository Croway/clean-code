package it.croway.project;

import it.croway.core.http.HttpVerb;
import it.croway.core.http.Path;

@Path(value = "other", verb = HttpVerb.GET)
public class OtherResource {

	@Path(value = "", verb = HttpVerb.GET)
	public String test() {
		return "other resource";
	}
}
