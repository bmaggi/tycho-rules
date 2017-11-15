# List of available rules

## Rule CheckManifestParameter

Check that a specific *field* in the manifest.mf is equals to a predefined *value*
Example : Check that *Bundle-Localization* is using the correct property file *plugin*

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.CheckManifestParameter">
	<field>Bundle-Localization</field>
	<value>plugin</value>
</myCustomRule>
```  

## Rule RequireBundleVersion

Check that all required bundle have *bundle-version* set.

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.RequireBundleVersion">
</myCustomRule>
```  

## Rule RequireBundleOrder

Check that required bundle are following a specific order.
Parameter : 
 - order : the expected order by namespace, all non listed namespace will be at the end
 - inverse : inverse the order, useful to pill up unreferenced namespace at the top

Example : Check that plugins starting by *org.eclipse.ui* are imported before plugins starting by *org.eclipse.core* 

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.RequireBundleOrder">
	<order>
		<namespace>org.eclipse.ui</namespace>
		<namespace>org.eclipse.core</namespace>
	</order>
	<inverse>false</inverse>
</myCustomRule>
```  

## Rule CheckExportPackage

Check that all package are exported

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.CheckExportPackage">
</myCustomRule>
```  

## Rule 5 CheckReexportBundle

Check that the bundle contained in the namespace is reexported

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.CheckReexportBundle">
	<namespace>org.eclipse.ui</namespace>
</myCustomRule>
```  
## Rule ManifestHeaderOrder

Check that the manifest is following the standard order.
This order is hard-coded for the moment.

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.ManifestHeaderOrder">
</myCustomRule>
```  

## Rule RequireFeatureProvider

Check the feature provider-name (vendor)
( Should change to check different values in the feature.xml file)

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.RequireFeatureProvider">
		<provider>myself</provider>
</myCustomRule>
```  

## Rule ExtensionPointDocumentation

Check the extension point to ensure that there is documentation

```xml
<myCustomRule implementation="com.github.bmaggi.tycho.rules.ExtensionPointDocumentation">
		<provider>myself</provider>
</myCustomRule>
```  