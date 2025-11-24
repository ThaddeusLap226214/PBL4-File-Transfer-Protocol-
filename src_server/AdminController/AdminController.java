package AdminController;

import Model.ModelBO;
import View.ViewAdminConfigure;

public class AdminController {
	private ModelBO BO = new ModelBO();
	private ViewAdminConfigure view;
	
	public AdminController(ViewAdminConfigure viewConfig) {
		this.view = viewConfig;
	}

}
