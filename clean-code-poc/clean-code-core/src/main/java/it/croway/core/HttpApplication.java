package it.croway.core;

public abstract class HttpApplication {

	DIApplication diApplication;

	public HttpApplication(DIApplication diApplication) {
		this.diApplication = diApplication;
	}

	protected abstract void register();

	public DIApplication getDiApplication() {
		return diApplication;
	}

	public void setDiApplication(DIApplication diApplication) {
		this.diApplication = diApplication;
	}

}
