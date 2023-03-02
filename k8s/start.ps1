../gradlew clean -p ../

../gradlew buildFull -p ../ -D'quarkus.package.type'=native -D'quarkus.profile'=native
