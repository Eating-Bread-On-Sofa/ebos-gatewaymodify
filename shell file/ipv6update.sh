#!/bin/sh

name=$1
ipv6=$2
masknum=$3
gateway6=$4
ipv4=$5
netmask=$6
gateway4=$7
ips=$8
ipv6s=$9

cd /etc/network/interfaces.d
: > $name
cat >> $name << EOF
#iface $name inet manual
auto $name
allow-hotplug $name
iface $name inet6 static
        address $ipv6
        netmask $masknum
        gateway $gateway6
iface $name inet static
	address $ipv4
	netmask $netmask
	gateway $gateway4
EOF
ifconfig $name inet6 add $ipv6s up
ifconfig $name $ips up
echo "修改成功"
