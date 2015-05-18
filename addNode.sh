#!/bin/bash 
DEMO="JDG MapReduce Demo"
AUTHORS="Pablo Szuster, pszuster@redhat.com"
JBOSS_HOME=./target/masterNode/jboss-eap-6.4
SRC_DIR=./installs
EAP_SERVER=jboss-eap-6.4.0.zip
JDG_LIBRARY_MODULES=jboss-datagrid-6.4.1-eap-modules-library.zip
USER=admin
PASSWORD=admin-123

function print_header() {
	# wipe screen.
	clear 
	echo

	ASCII_WIDTH=56

	printf "##  %-${ASCII_WIDTH}s  ##\n" | sed -e 's/ /#/g'
	printf "##  %-${ASCII_WIDTH}s  ##\n"   
	printf "##  %-${ASCII_WIDTH}s  ##\n" "Setting up the ${DEMO}"
	printf "##  %-${ASCII_WIDTH}s  ##\n"
	printf "##  %-${ASCII_WIDTH}s  ##\n"
	printf "##  %-${ASCII_WIDTH}s  ##\n" "    # ####   ###   ###  ###   ###   ###"
	printf "##  %-${ASCII_WIDTH}s  ##\n" "    # #   # #   # #    #      #  # #"
	printf "##  %-${ASCII_WIDTH}s  ##\n" "    # ####  #   #  ##   ##    #  # #  ##"
	printf "##  %-${ASCII_WIDTH}s  ##\n" "#   # #   # #   #    #    #   #  # #   #"
	printf "##  %-${ASCII_WIDTH}s  ##\n" " ###  ####   ###  ###  ###    ###   ###"  
	printf "##  %-${ASCII_WIDTH}s  ##\n"
	printf "##  %-${ASCII_WIDTH}s  ##\n"
	printf "##  %-${ASCII_WIDTH}s  ##\n"   
	printf "##  %-${ASCII_WIDTH}s  ##\n" "brought to you by,"
	printf "##  %-${ASCII_WIDTH}s  ##\n" "  ${AUTHORS}"
	printf "##  %-${ASCII_WIDTH}s  ##\n"
	printf "##  %-${ASCII_WIDTH}s  ##\n"
	printf "##  %-${ASCII_WIDTH}s  ##\n" | sed -e 's/ /#/g'
}


function print_usage() {
	echo "This a init script for setting up $DEMO"
	echo "usage: $0 [--nodeNumber[=]<value>]" >&2	
	echo "    --nodeNumber=<value> is the node number"
}

function setup_eap_with_modules() {
	# make some checks first before proceeding.	
	DOWNLOADS=($EAP_SERVER $JDG_LIBRARY_MODULES)

	for DONWLOAD in ${DOWNLOADS[@]}
	do
		if [[ -r $SRC_DIR/$DONWLOAD || -L $SRC_DIR/$DONWLOAD ]]; then
				echo $DONWLOAD are present...
				echo
		else
				echo You need to download $DONWLOAD from the Customer Support Portal 
				echo and place it in the $SRC_DIR directory to proceed...
				echo
				exit
		fi
	done

	# Create the JBOSS_HOME directory if it does not already exist.
	if [ ! -x ${JBOSS_HOME} ]; then
			echo "  - creating the target directory..."
			echo
			mkdir -p ${JBOSS_HOME}
	else
			echo "  - detected target directory, moving on..."
			echo
	fi

	pushd ${JBOSS_HOME}/.. >/dev/null
	EXTRACT_DIR=`pwd`
	popd >/dev/null

	echo Unpacking new JBoss Enterprise EAP 6...
	echo
		
	unzip -q -o -d ${EXTRACT_DIR} $SRC_DIR/$EAP_SERVER

	# Creating and admin user
	echo "Adding admin user"
	$JBOSS_HOME/bin/add-user.sh -g admin -u $USER -p $PASSWORD -s


	# Adding JBoss Data Grid Library modules to EAP
	echo "Adding JBoss Data Grid Modules to EAP"
	tmpdir=`mktemp -d XXXXXXXX`
	unzip -q -d ${tmpdir} ${SRC_DIR}/${JDG_LIBRARY_MODULES}
	cp -R ${tmpdir}/jboss-datagrid-6.4.1-eap-modules-library/modules/* $JBOSS_HOME/modules/
	rm -rf  ${tmpdir} 
	
	echo "Done setting up EAP with modules"	
}

function setup_eap_node_with_modules() {
	NODE_NAME=$1

	ORG_JBOSS_HOME=$JBOSS_HOME
	
	JBOSS_HOME=./target/${NODE_NAME}/jboss-eap-6.4
	
	setup_eap_with_modules
	
	# Reset JBOSS_HOME to it's original value
	JBOSS_HOME=$ORG_JBOSS_HOME
		
}




#### Start the script

print_header

if [ $# -eq 0 ]; then
	echo "$0 requires an argument"
	print_usage
	exit 2
fi

NODECOUNT=1

while getopts ":h-:" opt; do
  	case "${opt}" in
        -)
            case "${OPTARG}" in
                nodeNumber=*)
                    val=${OPTARG#*=}
                    opt=${OPTARG%=$val}
                    #echo "Parsing option: '--${opt}', value: '${val}'" >&2
                    NODENUMBER=${val}
                    ;;
            	*)
                    if [ "$OPTERR" = 1 ] && [ "${optspec:0:1}" != ":" ]; then
                        echo "Unknown option --${OPTARG}" >&2
                        print_usage
                        exit 2
                    fi
                    ;;
            esac;;
        h)
            print_usage
            exit
            ;;
        *)
            if [ "$OPTERR" != 1 ] || [ "${optspec:0:1}" = ":" ]; then
                echo "Non-option argument: '-${OPTARG}'" >&2
                print_usage
                exit 2
            fi
            ;;
    esac
done




echo
echo "Setting up the ${DEMO} environment"
echo
		echo "Setting up Node$NODENUMBER"
		setup_eap_node_with_modules "Node$NODENUMBER"
		echo "Starting Node$NODENUMBER..."
		./target/${NODE_NAME}/jboss-eap-6.4/bin/standalone.sh -Djboss.node.name=node$NODENUMBER -Djboss.socket.binding.port-offset=$((NODENUMBER*100))> /dev/null &
		sleep 10s
		echo "Deploying MR app to Node$NODENUMBER..."
		cd projects
		mvn clean package -pl JDG_MR jboss-as:deploy -pl JDG_MR -Djboss.as-hostname=127.0.0.1 -Djboss-as.port=$((9999+(NODENUMBER*100))) -Djboss-as.username=$USER -Djboss-as.password=$PASSWORD
		cd ..
		echo "MR App running on Node$NODENUMBER"
	echo "Node$NODENUMBER Setup completed!"
