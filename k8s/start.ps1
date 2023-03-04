k3d registry create myregistry.localhost --port 12345
k3d cluster create newcluster --registry-use k3d-myregistry.localhost:12345 --api-port 127.0.0.1:6443 -p 80:80@loadbalancer -p 443:443@loadbalancer

../gradlew clean -p ../

../gradlew buildFull -p ../ -D'quarkus.package.type'=native -D'quarkus.profile'=native
