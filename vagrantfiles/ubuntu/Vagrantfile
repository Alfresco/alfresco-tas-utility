# -*- mode: ruby -*-
# vi: set ft=ruby :

$scriptubuntu = <<SCRIPT
    cd /vagrant
    echo $LDAP_USER
    mvn clean install -s $HOME/.m2/settings.xml -DskipTests
SCRIPT


Vagrant.configure('2') do |config|

    config.vm.define "ubuntu" do |ubuntu|
      ubuntu.vm.box = "ubuntu"
      ubuntu.vm.box_url = "https://s3-eu-west-1.amazonaws.com/tasartifacts/vagrantUI/ubuntu/ubuntu.json"
        ubuntu.vm.provider "virtualbox" do |vb|
          vb.memory = "4500"
          vb.cpus = "2"
          vb.customize ['modifyvm', :id, '--cableconnected1', 'on']
        end

        ubuntu.vm.provision 'shell', env: {"LDAP_USER" => "#{ENV['AUTHUSER']}", "LDAP_PASS" => "#{ENV['AUTHPASS']}"}, inline: $scriptubuntu, privileged: false
    end

end
