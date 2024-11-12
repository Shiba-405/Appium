Feature: Appium Testing

	Ecom App Login invalid Credential

	@UI-smoke2
	Scenario: smoke2 - As a User I should be able to login with invalid credentials
		Given User is on login page for TestCase "UI-smoke2"
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
		Then User Enters value "testcodeelan@gmail.com" in  "Text Field" "Email" having "id" "com.studiobluelime.ecommerceapp:id/et_login_username"
		Then User Enters value "Shiba#555" in  "Text Field" "Password" having "id" "com.studiobluelime.ecommerceapp:id/et_login_password"
		Then User Clicks element "LOGIN" "Button" having "id" "com.studiobluelime.ecommerceapp:id/btn_login"
