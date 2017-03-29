#!groovy
podTemplate(label: 'storedqf', containers: [
        containerTemplate(name: 'jnlp', image: 'henryrao/jnlp-slave', args: '${computer.jnlpmac} ${computer.name}', alwaysPullImage: true),
        containerTemplate(name: 'kubectl', image: 'henryrao/kubectl:1.5.2', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'sbt', image: 'henryrao/sbt:211', ttyEnabled: true, command: 'cat', alwaysPullImage: true),
        containerTemplate(name: 'docker', image: 'docker:1.12.6', ttyEnabled: true, command: 'cat')
],
        volumes: [
                hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
                hostPathVolume(mountPath: '/root/.kube/config', hostPath: '/root/.kube/config'),
                persistentVolumeClaim(claimName: 'jenkins-ivy2', mountPath: '/home/jenkins/.ivy2', readOnly: false)
        ],
        workspaceVolume: emptyDirWorkspaceVolume(false)
) {
    node('storedqf') {
        ansiColor('xterm') {
            checkout scm
            def elasticsearchContainerId
            stage('prepare') {
                container('docker') {
                    elasticsearchContainerId = sh(returnStdout: true, script: 'docker run -d -p 9200:9200 -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" -e "xpack.security.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:5.2.2').trim()
                    def ip = sh(returnStdout: true, script: "docker inspect --format='{{.NetworkSettings.IPAddress}}' ${elasticsearchContainerId}").trim()

                    timeout(time: 30, unit: 'SECONDS') {
                        waitUntil {
                            def r = sh script: "curl http://${ip}:9200", returnStatus: true
                            return (r == 0)
                        }
                    }
                }
            }
            stage('compile') {
                container('sbt') {
                    sh 'sbt compile'
                }
            }
            stage('unit test') {
                container('sbt') {
                    sh 'sbt test'
                }
            }
            def imageSha
            stage('build image') {
                container('sbt') {
                    sh 'sbt cpJarsForDocker'
                }
                dir('target/docker') {
                    container('docker') {
                        def mainClass = sh(returnStdout: true, script: 'cat mainClass').trim()
                        imageSha = sh(returnStdout: true, script: "docker build --pull --build-arg mainClass=${mainClass} -q .").trim()[7..-1]
                    }
                }
            }
            stage('test') {
                container('docker') {
                    def containerId = sh(returnStdout: true, script: "docker run -d ${imageSha}")
                    sleep 10
                    sh "docker logs ${containerId}"
                    sh "docker rm -f -v ${containerId}"
                }

            }
            stage('tear down') {
                container('docker') {
                    sh "docker rm -f -v ${elasticsearchContainerId}"
                }
            }
            step([$class: 'LogParserPublisher', failBuildOnError: true, unstableOnWarning: true, showGraphs: true,
                  projectRulePath: 'jenkins-rule-logparser', useProjectRule: true])
        }

    }

}