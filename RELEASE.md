## How to make a release
### Check that you are using latest version
```  
mvn versions:display-dependency-updates
mvn versions:display-plugin-updates
```  

### To release on maven central.
```  
mvn release:clean release:prepare 
```  
follow by
```  
mvn release:perform
```  