Feature: Appium Testing

	Ecom App Register

	@UI-ecom
	Scenario: ecom - As a User I should be able to register
		Given User is on login page for TestCase "UI-ecom"
		Then User Clicks element if present "alert" "" having "id" "android:id/aerr_wait"
		Then User Clicks element if present "alert" "" having "id" "android:id/aerr_wait"
		Then User Verifies element "Logo" "icon" having "class" "android.widget.ImageView"
		Then User Verifies element "Company Name" "Text" having "xpath" "//android.widget.TextView[@text='E Commerce App']"
		Then User Verifies text of "Slogan" "Text" Should be "The Easiest Way To Order Products From This App !" having "xpath" "//android.widget.TextView[2]"
		Then User Clicks element "Skip" "Button" having "id" "com.studiobluelime.ecommerceapp:id/btn_skip"
		Then User Verifies text of "Title" "Text" Should be "eCommerce App" having "class" "android.widget.TextView"
		Then User Verifies element "Menu" "Icon" having "accessibility id" "App"
		Then User Clicks element "Menu" "Icon" having "accessibility id" "App"
		Then User Verifies element in List "New Arrivals" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Sale" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Filter" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "My Account" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Cart" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Share App" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Notifications" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Info" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Social" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Verifies element in List "Contact Us" in "Menu" "Text" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text']"
		Then User Clicks element "My Account" "Anchor Link" having "xpath" "//android.widget.CheckedTextView[@resource-id='com.studiobluelime.ecommerceapp:id/design_menu_item_text' and @text='My Account']"
		Then User Verifies element "Email" "Text Field" having "id" "com.studiobluelime.ecommerceapp:id/et_login_username"
		Then User Verifies element "Password" "Text Field" having "id" "com.studiobluelime.ecommerceapp:id/et_login_password"
		Then User Verifies element "LOGIN" "Button" having "id" "com.studiobluelime.ecommerceapp:id/btn_login"
		Then User Verifies element "Forgot Password ??" "Link" having "id" "com.studiobluelime.ecommerceapp:id/tv_reset_password"
		Then User Verifies element "Not Registered ? Register Now !" "Link" having "id" "com.studiobluelime.ecommerceapp:id/tv_register"
		Then User Clicks element "Not Registered ? Register Now !" "Link" having "id" "com.studiobluelime.ecommerceapp:id/tv_register"
		Then User Verifies element "Name" "Text Field" having "id" "com.studiobluelime.ecommerceapp:id/et_register_username"
		Then User Verifies element "Mobile Number" "Text Field" having "id" "com.studiobluelime.ecommerceapp:id/et_register_mno"
		Then User Verifies element "Email" "Text Field" having "id" "com.studiobluelime.ecommerceapp:id/et_register_email"
		Then User Verifies element "Password" "Text Field" having "id" "com.studiobluelime.ecommerceapp:id/et_register_password"
		Then User Verifies element "Show password" "Icon" having "accessibility id" "Show password"
		Then User Verifies element "REGISTER" "Button" having "id" "com.studiobluelime.ecommerceapp:id/btn_register"
		Then User Verifies element "Already Registered ? Login Now !" "Link" having "id" "com.studiobluelime.ecommerceapp:id/tv_login"
		Then User Enters value "Shiba" in  "Text Field" "Name" having "id" "com.studiobluelime.ecommerceapp:id/et_register_username"
		Then User Enters value "8908272957" in  "Text Field" "Mobile Number" having "id" "com.studiobluelime.ecommerceapp:id/et_register_mno"
		Then User Enters value "testcodeelan@gmail.com" in  "Text Field" "Email" having "id" "com.studiobluelime.ecommerceapp:id/et_register_email"
		Then User Enters value "Shiba#55" in  "Text Field" "Password" having "id" "com.studiobluelime.ecommerceapp:id/et_register_password"
		Then User Clicks element "Show password" "Icon" having "accessibility id" "Show password"
		Then User Clicks element "REGISTER" "Button" having "id" "com.studiobluelime.ecommerceapp:id/btn_register"
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
