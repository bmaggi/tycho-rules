# tycho-rules
A [Maven][1]/[Tycho][2] project to add some validation rules for [OSGI][3] development.
Most of the rules are pretty simple and are used to maintain coherence in a multi-plugin project.

# Status
[![Codeship Status for bmaggi/tycho-rules](https://app.codeship.com/projects/ecdb8d10-ac38-0135-0738-52e8b96e2dec/status?branch=master)](https://app.codeship.com/projects/256918)
[![License](https://img.shields.io/badge/license-EPL2-blue.svg)](https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.bmaggi.checks/tycho-rules/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.bmaggi.checks/tycho-rules)

## How to build

This project is built using Maven.
To build locally, simply execute the command line:

```
mvn clean install
```

You can also chose the it test with this command 

```
mvn invoker:run -Dinvoker.test=checkexportpackage,checkexportpackage.failing
```

## How to use

Configuration to add in the pom.xml of your project:
```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-enforcer-plugin</artifactId>
			<version>${enforcer.api.version}</version>
			<dependencies>
				<dependency>
					<groupId>com.github.bmaggi.checks</groupId>
					<artifactId>tycho-rules</artifactId>
					<version>0.1.3</version>
				</dependency>
			</dependencies>
			<executions>
				<execution>
					<id>custom-enforce</id>
					<phase>validate</phase>
					<goals>
						<goal>enforce</goal>
					</goals>
					<configuration>
						<rules>
						<!-- define your rules here see Rules.md -->
						</rules>
					</configuration>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```  


[1]:https://maven.apache.org/
[2]:https://eclipse.org/tycho/
[3]:http://www.osgi.org/
