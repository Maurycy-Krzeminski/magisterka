k3d cluster create newcluster --api-port 127.0.0.1:6443 -p 80:80@loadbalancer -p 443:443@loadbalancer

kubectl create -f https://raw.githubusercontent.com/keycloak/keycloak-quickstarts/latest/kubernetes-examples/keycloak.yaml
kubectl apply -f .\keycloak-ingress.yaml

../gradlew.bat buildFull -p ../

kubectl apply -f ..\dsa\build\kubernetes\kubernetes.yml
kubectl apply -f ..\math\build\kubernetes\kubernetes.yml
kubectl apply -f ..\mba\build\kubernetes\kubernetes.yml
