/*
 * SPDX-License-Identifier: Apache-2.0
 */

package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"strconv"
	"strings"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// Chaincode is the definition of the chaincode structure.
type Chaincode struct {
}

//需求方
type demand struct {
	ObjectType   string  `json:"docType"`
	DemandID     int     `json:"demandID"`
	Name         string  `json:"name"`
	Category     string  `json:"category"`
	AmountNeeded int     `json:"amountNeeded"`
	Unit         string  `json:"unit"`
	Priority     int     `json:"priority"`
	DemanderID   int     `json:"demanderID"`
	Lat          float64 `json:"lat"`
	Lon          float64 `json:"lon"`
}

//供应方有费池
type profitablesupply struct {
	ObjectType   string  `json:"docType"`
	SupplyID     int     `json:"supplyID"`
	Name         string  `json:"name"`
	Amount       int     `json:"amount"`
	Unit         string  `json:"unit"`
	ProviderID   int     `json:"providerID"`
	UnitPrice    float64 `json:"unitprice"`
	ProviderRank int     `json:"providerRank"`
	Lat          float64 `json:"lat"`
	Lon          float64 `json:"lon"`
	CoverRadius  float64 `json:"coverRadius"`
}

//供应方无费池
type unprofitablesupply struct {
	ObjectType   string  `json:"docType"`
	SupplyID     int     `json:"supplyID"`
	Name         string  `json:"name"`
	Amount       float64 `json:"amount"`
	Unit         string  `json:"unit"`
	ProviderID   int     `json:"providerID"`
	ProviderRank int     `json:"providerRank"`
	Lat          float64 `json:"lat"`
	Lon          float64 `json:"lon"`
	CoverRadius  float64 `json:"coverRadius"`
}

//供应方
type organization struct {
	ObjectType string  `json:"docType"`
	OrgID      int     `json:"orgID"`
	Name       string  `json:"name"`
	Score      int     `json:"score"`
	Rank       int     `json:"rank"`
	OrgType    string  `json:"orgType"`
	DefaultLat float64 `json:"defaultLat"`
	DefaultLon float64 `json:"defaultLon"`
}

type provider struct {
	ObjectType         string  `json:"docType"`
	OrgID              int     `json:"orgID"`
	Name               string  `json:"name"`
	Score              int     `json:"score"`
	Rank               int     `json:"rank"`
	OrgType            string  `json:"orgType"`
	DefaultLat         float64 `json:"defaultLat"`
	DefaultLon         float64 `json:"defaultLon"`
	DefaultCoverRadius float64 `json:"defaultCoverRadius"`
}

// Init is called when the chaincode is instantiated by the blockchain network.
func (cc *Chaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

// Invoke is called as a result of an application request to run the chaincode.
func (cc *Chaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fcn, args := stub.GetFunctionAndParameters()

	if fcn == "initDemand" {
		return cc.initDemand(stub, args)
	} else if fcn == "initProfitablesupply" {
		return cc.initProfitablesupply(stub, args)
	} else if fcn == "initUnprofitablesupply" {
		return cc.initUnprofitablesupply(stub, args)
	} else if fcn == "initOrganization" {
		return cc.initOrganization(stub, args)
	} else if fcn == "initProvider" {
		return cc.initProvider(stub, args)
	} else if fcn == "queryByKey" {
		return cc.queryByKey(stub, args)
	} else if fcn == "queryProByName" {
		return cc.queryProByName(stub, args)
	} else if fcn == "queryUnproByName" {
		return cc.queryUnproByName(stub, args)
	} else if fcn == "queryProByOrganization" {
		return cc.queryProByOrganization(stub, args)
	} else if fcn == "queryUnproByOrganization" {
		return cc.queryUnproByOrganization(stub, args)
	} else if fcn == "set" {
		return cc.set(stub, args)
	} else if fcn == "updateDemandamount" {
		return cc.updateDemandamount(stub, args)
	} else if fcn == "updateProfitablesupplyamount" {
		return cc.updateProfitablesupplyamount(stub, args)
	} else if fcn == "updateUnprofitablesupplyamount" {
		return cc.updateUnprofitablesupplyamount(stub, args)
	} else if fcn == "updateOrganization" {
		return cc.updateOrganization(stub, args)
	} else if fcn == "delete" {
		return cc.delete(stub, args)
	}
	return shim.Error("Invoke fcn error")
}

//创建需求方信息，7个参数
func (cc *Chaincode) initDemand(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 9 {
		return shim.Error("not enough args")
	}
	demandID, _ := strconv.Atoi(args[0])
	name := strings.ToLower(args[1])
	category := strings.ToLower(args[2])
	amountNeeded, _ := strconv.Atoi(args[3])
	unit := strings.ToLower(args[4])
	priority, _ := strconv.Atoi(args[5])
	demanderID, _ := strconv.Atoi(args[6])
	lat, _ := strconv.ParseFloat(args[7], 64)
	lon, _ := strconv.ParseFloat(args[8], 64)

	// ==== Check if demand already exists ====
	demandAsBytes, err := stub.GetState(strconv.Itoa(demandID))
	if err != nil {
		return shim.Error("Failed to get demand: " + err.Error())
	} else if demandAsBytes != nil {
		fmt.Println("This demand already exists: " + strconv.Itoa(demandID))
		return shim.Error("This demand already exists: " + strconv.Itoa(demandID))
	}
	// ==== Create demond object and marshal to JSON ====
	objectType := "demand"
	demand := &demand{objectType, demandID, name, category, amountNeeded, unit, priority, demanderID, lat, lon}
	demandJSONasBytes, err := json.Marshal(demand)
	if err != nil {
		return shim.Error(err.Error())
	}
	// === Save demand to state ===
	err = stub.PutState(strconv.Itoa(demandID), demandJSONasBytes)
	if err != nil {
		return shim.Error(err.Error())
	}
	// ==== Marble saved and indexed. Return success ====
	fmt.Println("- end init demand")
	return shim.Success(nil)
}

//创建供应方有费池信息，6个参数
func (cc *Chaincode) initProfitablesupply(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 10 {
		return shim.Error("not enough args")
	}
	supplyID, _ := strconv.Atoi(args[0])
	name := strings.ToLower(args[1])
	amount, _ := strconv.Atoi(args[2])
	unit := strings.ToLower(args[3])
	providerID, _ := strconv.Atoi(args[4])
	unitPrice, _ := strconv.ParseFloat(args[5], 64)
	providerRank, _ := strconv.Atoi(args[6])
	lat, _ := strconv.ParseFloat(args[7], 64)
	lon, _ := strconv.ParseFloat(args[8], 64)
	coverRadius, _ := strconv.ParseFloat(args[9], 64)
	// ==== Check if profitablesupply already exists ====
	profitablesupplyAsBytes, err := stub.GetState(strconv.Itoa(supplyID))
	if err != nil {
		return shim.Error("Failed to get profitablesupply: " + err.Error())
	} else if profitablesupplyAsBytes != nil {
		fmt.Println("This profitablesupply already exists: " + strconv.Itoa(supplyID))
		return shim.Error("This profitablesupply already exists: " + strconv.Itoa(supplyID))
	}
	// ==== Create demond object and marshal to JSON ====
	docType := "profitablesupply"
	profitablesupply := &profitablesupply{docType, supplyID, name, amount, unit, providerID, unitPrice, providerRank, lat, lon, coverRadius}
	profitablesupplyJSONasBytes, err := json.Marshal(profitablesupply)
	if err != nil {
		return shim.Error(err.Error())
	}
	// === Save demand to state ===
	err = stub.PutState(strconv.Itoa(supplyID), profitablesupplyJSONasBytes)
	if err != nil {
		return shim.Error(err.Error())
	}
	indexName1 := "name~psupplyID"
	namePsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName1, []string{profitablesupply.Name, strconv.Itoa(profitablesupply.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value
	//value1 := []byte{0x00}
	stub.PutState(namePsupplyIDIndexKey, profitablesupplyJSONasBytes)

	indexName2 := "organization~psupplyID"
	organizationPsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName2, []string{strconv.Itoa(profitablesupply.ProviderID), strconv.Itoa(profitablesupply.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value

	stub.PutState(organizationPsupplyIDIndexKey, profitablesupplyJSONasBytes)
	// ==== Marble saved and indexed. Return success ====
	fmt.Println("- end init profitablesupply")
	return shim.Success(nil)
}

//创建供应方无费池信息，5个参数
func (cc *Chaincode) initUnprofitablesupply(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 9 {
		return shim.Error("not enough args")
	}
	supplyID, _ := strconv.Atoi(args[0])
	name := strings.ToLower(args[1])
	amount, _ := strconv.ParseFloat(args[2], 64)
	unit := strings.ToLower(args[3])
	providerID, _ := strconv.Atoi(args[4])
	providerRank, _ := strconv.Atoi(args[5])
	lat, _ := strconv.ParseFloat(args[6], 64)
	lon, _ := strconv.ParseFloat(args[7], 64)
	coverRadius, _ := strconv.ParseFloat(args[8], 64)
	// ==== Check if unprofitablesupply already exists ====
	unprofitablesupplyAsBytes, err := stub.GetState(strconv.Itoa(supplyID))
	if err != nil {
		return shim.Error("Failed to get unprofitablesupply: " + err.Error())
	} else if unprofitablesupplyAsBytes != nil {
		fmt.Println("This unprofitablesupply already exists: " + strconv.Itoa(supplyID))
		return shim.Error("This unprofitablesupply already exists: " + strconv.Itoa(supplyID))
	}
	// ==== Create demond object and marshal to JSON ====
	objectType := "unprofitablesupply"
	unprofitablesupply := &unprofitablesupply{objectType, supplyID, name, amount, unit, providerID, providerRank, lat, lon, coverRadius}
	unprofitablesupplyJSONasBytes, err := json.Marshal(unprofitablesupply)
	if err != nil {
		return shim.Error(err.Error())
	}
	// === Save demand to state ===
	err = stub.PutState(strconv.Itoa(supplyID), unprofitablesupplyJSONasBytes)
	if err != nil {
		return shim.Error(err.Error())
	}
	indexName1 := "name~unpsupplyID"
	nameUnpsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName1, []string{unprofitablesupply.Name, strconv.Itoa(unprofitablesupply.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value
	//value1 := []byte{0x00}
	stub.PutState(nameUnpsupplyIDIndexKey, unprofitablesupplyJSONasBytes)

	indexName2 := "organization~unpsupplyID"
	organizationUnpsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName2, []string{strconv.Itoa(unprofitablesupply.ProviderID), strconv.Itoa(unprofitablesupply.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value

	stub.PutState(organizationUnpsupplyIDIndexKey, unprofitablesupplyJSONasBytes)
	// ==== Marble saved and indexed. Return success ====
	fmt.Println("- end init unprofitablesupply")
	return shim.Success(nil)
}

//创建供应方
func (cc *Chaincode) initOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 7 {
		return shim.Error("not enough args")
	}
	orgID, _ := strconv.Atoi(args[0])
	name := strings.ToLower(args[1])
	score, _ := strconv.Atoi(args[2])
	rank, _ := strconv.Atoi(args[3])
	orgType := strings.ToLower(args[4])
	defaultLat, _ := strconv.ParseFloat(args[5], 64)
	defaultLon, _ := strconv.ParseFloat(args[6], 64)

	// ==== Check if unprofitablesupply already exists ====
	organizationAsBytes, err := stub.GetState(strconv.Itoa(orgID))
	if err != nil {
		return shim.Error("Failed to get organization: " + err.Error())
	} else if organizationAsBytes != nil {
		fmt.Println("This organization already exists: " + strconv.Itoa(orgID))
		return shim.Error("This organization already exists: " + strconv.Itoa(orgID))
	}
	// ==== Create demond object and marshal to JSON ====
	objectType := "organization"
	organization := &organization{objectType, orgID, name, score, rank, orgType, defaultLat, defaultLon}
	organizationJSONasBytes, err := json.Marshal(organization)
	if err != nil {
		return shim.Error(err.Error())
	}
	// === Save demand to state ===
	err = stub.PutState(strconv.Itoa(orgID), organizationJSONasBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// ==== Marble saved and indexed. Return success ====
	fmt.Println("- end init organization")
	return shim.Success(nil)
}

func (cc *Chaincode) initProvider(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 8 {
		return shim.Error("not enough args")
	}
	orgID, _ := strconv.Atoi(args[0])
	name := strings.ToLower(args[1])
	score, _ := strconv.Atoi(args[2])
	rank, _ := strconv.Atoi(args[3])
	orgType := strings.ToLower(args[4])
	defaultLat, _ := strconv.ParseFloat(args[5], 64)
	defaultLon, _ := strconv.ParseFloat(args[6], 64)
	defaultCoverRadius, _ := strconv.ParseFloat(args[7], 64)

	// ==== Check if unprofitablesupply already exists ====
	providerAsBytes, err := stub.GetState(strconv.Itoa(orgID))
	if err != nil {
		return shim.Error("Failed to get provider: " + err.Error())
	} else if providerAsBytes != nil {
		fmt.Println("This provider already exists: " + strconv.Itoa(orgID))
		return shim.Error("This provider already exists: " + strconv.Itoa(orgID))
	}

	// ==== Create demond object and marshal to JSON ====
	objectType := "provider"
	provider := &provider{objectType, orgID, name, score, rank, orgType, defaultLat, defaultLon, defaultCoverRadius}
	providerJSONasBytes, err := json.Marshal(provider)
	if err != nil {
		return shim.Error(err.Error())
	}
	// === Save demand to state ===
	err = stub.PutState(strconv.Itoa(orgID), providerJSONasBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// ==== Marble saved and indexed. Return success ====
	fmt.Println("- end init organization")
	return shim.Success(nil)
}

//查询，1个参数，要查询的对象
func (cc *Chaincode) queryByKey(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var User string // Entities
	var err error

	if len(args) != 1 {
		return shim.Error(err.Error())
	}

	User = args[0]

	// Get the state from the ledger
	value, err := stub.GetState(User)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Println(string(value))
	return shim.Success([]byte(value))
}

//在无费池中按Name查找
func (cc *Chaincode) queryUnproByName(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	name := strings.ToLower(args[0])

	fmt.Println("- start queryUnproByName ", name)

	// Query the color~name index by color
	// This will execute a key range query on all keys starting with 'color'
	nameUnpsupplyIDResultsIterator, err := stub.GetStateByPartialCompositeKey("name~unpsupplyID", []string{name})
	if err != nil {
		return shim.Error(err.Error())
	}
	defer nameUnpsupplyIDResultsIterator.Close()

	buffer, err := constructQueryResponseFromIterator(nameUnpsupplyIDResultsIterator)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return shim.Success([]byte(buffer.Bytes()))
}

//在有费池中按Name查找
func (cc *Chaincode) queryProByName(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	name := strings.ToLower(args[0])

	fmt.Println("- start queryProByName ", name)

	// Query the color~name index by color
	// This will execute a key range query on all keys starting with 'color'
	namePsupplyIDResultsIterator, err := stub.GetStateByPartialCompositeKey("name~psupplyID", []string{name})
	if err != nil {
		return shim.Error(err.Error())
	}
	defer namePsupplyIDResultsIterator.Close()

	buffer, err := constructQueryResponseFromIterator(namePsupplyIDResultsIterator)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

//在无费池中按Organization查找
func (cc *Chaincode) queryUnproByOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	organization := strings.ToLower(args[0])

	fmt.Println("- start queryUnproByOrganization ", organization)

	// Query the color~name index by color
	// This will execute a key range query on all keys starting with 'color'
	organizationUnpsupplyIDResultsIterator, err := stub.GetStateByPartialCompositeKey("organization~unpsupplyID", []string{organization})
	if err != nil {
		return shim.Error(err.Error())
	}
	defer organizationUnpsupplyIDResultsIterator.Close()

	buffer, err := constructQueryResponseFromIterator(organizationUnpsupplyIDResultsIterator)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

//在有费池中按Organization查找
func (cc *Chaincode) queryProByOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	organization := strings.ToLower(args[0])

	fmt.Println("- start queryProBqueryProByOrganizationyName ", organization)

	// Query the color~name index by color
	// This will execute a key range query on all keys starting with 'color'
	organizationPsupplyIDResultsIterator, err := stub.GetStateByPartialCompositeKey("organization~unpsupplyID", []string{organization})
	if err != nil {
		return shim.Error(err.Error())
	}
	defer organizationPsupplyIDResultsIterator.Close()

	buffer, err := constructQueryResponseFromIterator(organizationPsupplyIDResultsIterator)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return shim.Success([]byte(buffer.Bytes()))
}

func constructQueryResponseFromIterator(resultsIterator shim.StateQueryIteratorInterface) (*bytes.Buffer, error) {
	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		// Add a comma before array members, supress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	return &buffer, nil
}

//按Organization查找
func (cc *Chaincode) queryByOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	docType := strings.ToLower(args[0])
	organization := strings.ToLower(args[1])

	queryString := fmt.Sprintf("{\"selector\":{\"docType\":\"%s\",\"organization\":\"%s\"}}", docType, organization)

	queryResults, err := getQueryResultForQueryString(stub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}
func getQueryResultForQueryString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	fmt.Printf("- getQueryResultForQueryString queryString:\n%s\n", queryString)

	resultsIterator, err := stub.GetQueryResult(queryString)
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	buffer, err := constructQueryResponseFromIterator(resultsIterator)
	if err != nil {
		return nil, err
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return buffer.Bytes(), nil
}

//存储数据，2个参数  key，value
func (cc *Chaincode) set(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting key and value")
	}

	err := stub.PutState(args[0], []byte(args[1]))
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)

}

//更新需求方的需求数量
func (cc *Chaincode) updateDemandamount(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	demandID, _ := strconv.Atoi(args[0])
	newAmountNeeded, _ := strconv.Atoi(args[1])
	fmt.Println("- start updateDemandamount ", demandID, newAmountNeeded)

	demandAsBytes, err := stub.GetState(strconv.Itoa(demandID))
	if err != nil {
		return shim.Error("Failed to get demand:" + err.Error())
	} else if demandAsBytes == nil {
		return shim.Error("demand does not exist")
	}

	demandToUpdate := demand{}
	err = json.Unmarshal(demandAsBytes, &demandToUpdate) //unmarshal it aka JSON.parse()
	if err != nil {
		return shim.Error(err.Error())
	}
	demandToUpdate.AmountNeeded = newAmountNeeded //change the owner

	demandJSONasBytes, _ := json.Marshal(demandToUpdate)
	err = stub.PutState(strconv.Itoa(demandID), demandJSONasBytes) //rewrite the marble
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Println("- end updateDemandamount (success)")
	return shim.Success(nil)
}

//更新有费池
func (cc *Chaincode) updateProfitablesupplyamount(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	supplyID, _ := strconv.Atoi(args[0])
	newAmount, _ := strconv.Atoi(args[1])
	fmt.Println("- start updateProfitablesupplyamount ", strconv.Itoa(supplyID), newAmount)

	profitablesupplyAsBytes, err := stub.GetState(strconv.Itoa(supplyID))
	if err != nil {
		return shim.Error("Failed to get Profitablesupply:" + err.Error())
	} else if profitablesupplyAsBytes == nil {
		return shim.Error("Profitablesupply does not exist")
	}

	profitablesupplyToUpdate := profitablesupply{}
	err = json.Unmarshal(profitablesupplyAsBytes, &profitablesupplyToUpdate) //unmarshal it aka JSON.parse()
	if err != nil {
		return shim.Error(err.Error())
	}
	profitablesupplyToUpdate.Amount = newAmount //change the owner

	profitablesupplyJSONasBytes, _ := json.Marshal(profitablesupplyToUpdate)
	err = stub.PutState(strconv.Itoa(supplyID), profitablesupplyJSONasBytes) //rewrite the marble
	if err != nil {
		return shim.Error(err.Error())
	}
	indexName1 := "name~psupplyID"
	namePsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName1, []string{profitablesupplyToUpdate.Name, strconv.Itoa(profitablesupplyToUpdate.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value
	//value1 := []byte{0x00}
	stub.PutState(namePsupplyIDIndexKey, profitablesupplyJSONasBytes)
	indexName2 := "organization~psupplyID"
	organizationPsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName2, []string{strconv.Itoa(profitablesupplyToUpdate.ProviderID), strconv.Itoa(profitablesupplyToUpdate.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value

	stub.PutState(organizationPsupplyIDIndexKey, profitablesupplyJSONasBytes)

	fmt.Println("- end updateprofitablesupplyamount (success)")
	return shim.Success(nil)
}

//更新无费池
func (cc *Chaincode) updateUnprofitablesupplyamount(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	supplyID, _ := strconv.Atoi(args[0])
	newAmount, _ := strconv.ParseFloat(args[1], 64)
	fmt.Println("- start updateUnprofitablesupplyamount ", supplyID, newAmount)

	unprofitablesupplyAsBytes, err := stub.GetState(strconv.Itoa(supplyID))
	if err != nil {
		return shim.Error("Failed to get unprofitablesupply:" + err.Error())
	} else if unprofitablesupplyAsBytes == nil {
		return shim.Error("Unprofitablesupply does not exist")
	}

	unprofitablesupplyToUpdate := unprofitablesupply{}
	err = json.Unmarshal(unprofitablesupplyAsBytes, &unprofitablesupplyToUpdate) //unmarshal it aka JSON.parse()
	if err != nil {
		return shim.Error(err.Error())
	}
	unprofitablesupplyToUpdate.Amount = newAmount //change the owner

	unprofitablesupplyJSONasBytes, _ := json.Marshal(unprofitablesupplyToUpdate)
	err = stub.PutState(strconv.Itoa(supplyID), unprofitablesupplyJSONasBytes) //rewrite the marble
	if err != nil {
		return shim.Error(err.Error())
	}
	indexName1 := "name~unpsupplyID"
	nameUnpsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName1, []string{unprofitablesupplyToUpdate.Name, strconv.Itoa(unprofitablesupplyToUpdate.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value
	//value1 := []byte{0x00}
	stub.PutState(nameUnpsupplyIDIndexKey, unprofitablesupplyJSONasBytes)
	indexName2 := "organization~unpsupplyID"
	organizationUnpsupplyIDIndexKey, err := stub.CreateCompositeKey(indexName2, []string{strconv.Itoa(unprofitablesupplyToUpdate.ProviderID), strconv.Itoa(unprofitablesupplyToUpdate.SupplyID)})
	if err != nil {
		return shim.Error(err.Error())
	}
	//  Save index entry to state. Only the key name is needed, no need to store a duplicate copy of the marble.
	//  Note - passing a 'nil' value will effectively delete the key from state, therefore we pass null character as value

	stub.PutState(organizationUnpsupplyIDIndexKey, unprofitablesupplyJSONasBytes)
	fmt.Println("- end updateunprofitablesupplyamount (success)")
	return shim.Success(nil)
}

//更新Orgnization,score和rank。输入6个参数，如： OrgId  5 4 3 2 1 （6个参数，分别是OrgId 、5个指标平均的分数）
func (cc *Chaincode) updateOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var Score int // 分数
	var newScore int
	var newRank int
	var Xafter int                // 匹配后的分数
	var X, X1, X2, X3, X4, X5 int //分数相加
	var err error

	if len(args) < 6 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}

	orgID, _ := strconv.Atoi(args[0])
	fmt.Println("- start updateOrganization ", strconv.Itoa(orgID))
	orgnizationAsBytes, err := stub.GetState(strconv.Itoa(orgID))
	if err != nil {
		return shim.Error("Failed to get orgnization:" + err.Error())
	} else if orgnizationAsBytes == nil {
		return shim.Error("Orgnization does not exist")
	}
	organizationToUpdate := organization{}
	err = json.Unmarshal(orgnizationAsBytes, &organizationToUpdate) //unmarshal it aka JSON.parse()
	if err != nil {
		return shim.Error(err.Error())
	}
	Score = organizationToUpdate.Score

	X1, _ = strconv.Atoi(args[1])
	X2, _ = strconv.Atoi(args[2])
	X3, _ = strconv.Atoi(args[3])
	X4, _ = strconv.Atoi(args[4])
	X5, _ = strconv.Atoi(args[5])
	X = X1 + X2 + X3 + X4 + X5
	if X1 > 10 || X2 > 10 || X3 > 10 || X4 > 10 || X5 > 10 {
		return shim.Error("illegal grade")
	}

	if X >= 41 && X <= 50 {
		Xafter = 2
	} else if X >= 31 && X <= 40 {
		Xafter = 1
	} else if X == 30 {
		Xafter = 0
	} else if X >= 15 && X <= 29 {
		Xafter = -1
	} else if X >= 0 && X <= 14 {
		Xafter = -2
	} else {
		return shim.Error("illegal grade")
	}
	newScore = Score + Xafter

	if newScore > 75 && newScore <= 100 {
		newRank = 4
	} else if newScore > 50 && newScore <= 75 {
		newRank = 3
	} else if newScore > 25 && newScore <= 50 {
		newRank = 2
	} else if newScore > 0 && newScore <= 25 {
		newRank = 1
	}

	organizationToUpdate.Score = newScore //change the score
	organizationToUpdate.Rank = newRank   //change the rank

	organizationJSONasBytes, _ := json.Marshal(organizationToUpdate)
	err = stub.PutState(strconv.Itoa(orgID), organizationJSONasBytes) //rewrite the marble
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Println("- end updateunprofitablesupplyamount (success)")
	return shim.Success(nil)
}

//删除信息，一个参数
func (cc *Chaincode) delete(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.Expecting the key to delete")
	}

	err := stub.DelState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}
