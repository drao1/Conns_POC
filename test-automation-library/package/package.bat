call cd ..

call mvn update

call mvn clean install dependency:copy-dependencies -DskipTests

echo "Copied the dependencies. mvn:install completed successfully."

call ant package

echo "created jar file"

call mvn install:install-file -Dfile="./package/test-automation-library.jar" -DgroupId="com.etouch.taf" -DartifactId="test-automation-library" -Dversion="1.2" -Dpackaging="jar"

echo "copied the jar to maven local repository."

#PAUSE
