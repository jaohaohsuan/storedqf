#!groovy
podTemplate(label: 'storedqf', containers: [
        containerTemplate(name: 'jnlp', image: 'henryrao/jnlp-slave', args: '${computer.jnlpmac} ${computer.name}', alwaysPullImage: true),
        containerTemplate(name: 'kubectl', image: 'henryrao/kubectl:1.5.2', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'sbt', image: 'henryrao/sbt:211', ttyEnabled: true, command: 'cat', alwaysPullImage: true),
        containerTemplate(name: 'docker', image: 'docker:1.12.6', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'elasticsearch', image: 'docker.elastic.co/elasticsearch/elasticsearch:5.2.2', ttyEnabled: true, command: 'cat')
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
            stage('prepare') {
                container('elasticsearch') {
                    withEnv(['xpack.security.enabled=false', 'http.host=0.0.0.0', 'transport.host=127.0.0.1']) {
                        sh "/bin/bash /usr/share/elasticsearch/bin/es-docker &"
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
            stage('stage') {
                container('sbt') {
                    sh 'sbt cpJarsForDocker'
                }
            }
            def imageSha
            stage('build image') {
                dir('target/docker') {
                    container('docker') {
                        def mainClass = sh(returnStdout: true, script: 'cat mainClass').trim()
                        imageSha = sh(returnStdout: true, script: "docker build --pull --build-arg mainClass=${mainClass} -q .").trim()[7..-1]
                    }
                }
            }
            stage('test') {
                container('docker') {
                    sh "curl http://127.0.0.1:9200"
                    def containerId = sh(returnStdout: true, script: "docker run -d ${imageSha}")
                    sleep 10
                    sh "docker logs ${containerId}"
                    sh "docker rm -f -v ${containerId}"
                }

            }
            step([$class: 'LogParserPublisher', failBuildOnError: true, unstableOnWarning: true, showGraphs: true,
                  projectRulePath: 'jenkins-rule-logparser', useProjectRule: true])
        }

    }

}