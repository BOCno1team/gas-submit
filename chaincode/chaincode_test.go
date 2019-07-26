/*
 * SPDX-License-Identifier: Apache-2.0
 */

package main

import (
	"fmt"
	"testing"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/core/chaincode/shim/shimtest"
)

func mockInit(t *testing.T, stub *shimtest.MockStub, args [][]byte) {
	res := stub.MockInit("1", args)
	if res.Status != shim.OK {
		fmt.Println("Init failed", string(res.Message))
		t.FailNow()
	}
}

func initDemand(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("initDemand"), []byte(args[0]), []byte(args[1]), []byte(args[2]), []byte(args[3]), []byte(args[4]), []byte(args[5]), []byte(args[6])})
	if res.Status != shim.OK {
		fmt.Println("initDemand failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func initProfitablesupply(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("initProfitablesupply"), []byte(args[0]), []byte(args[1]), []byte(args[2]), []byte(args[3]), []byte(args[4]), []byte(args[5])})
	if res.Status != shim.OK {
		fmt.Println("initProfitablesupply failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func initUnprofitablesupply(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("initUnprofitablesupply"), []byte(args[0]), []byte(args[1]), []byte(args[2]), []byte(args[3]), []byte(args[4])})
	if res.Status != shim.OK {
		fmt.Println("initUnprofitablesupply failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func initOrganization(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("initOrganization"), []byte(args[0]), []byte(args[1]), []byte(args[2]), []byte(args[3]), []byte(args[4])})
	if res.Status != shim.OK {
		fmt.Println("initOrganization failed:", args[0], string(res.Message))
		t.FailNow()
	}
}

func queryByKey(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("queryByKey"), []byte(args[0])})
	if res.Status != shim.OK {
		fmt.Println("queryByKey failed:", args[0], string(res.Message))
		t.FailNow()
	}
}

func queryUnproByName(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("queryUnproByName"), []byte(args[0])})
	if res.Status != shim.OK {
		fmt.Println("queryUnproByName failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func queryUnproByOrganization(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("queryUnproByOrganization"), []byte(args[0])})
	if res.Status != shim.OK {
		fmt.Println("queryByUnproOrganization failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func updateDemandamount(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("updateDemandamount"), []byte(args[0]), []byte(args[1])})
	if res.Status != shim.OK {
		fmt.Println("updateDemandamount failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func updateProfitablesupplyamount(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("updateProfitablesupplyamount"), []byte(args[0]), []byte(args[1])})
	if res.Status != shim.OK {
		fmt.Println("updateProfitablesupplyamount failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func updateUnprofitablesupplyamount(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("updateUnprofitablesupplyamount"), []byte(args[0]), []byte(args[1])})
	if res.Status != shim.OK {
		fmt.Println("updateUnprofitablesupplyamount failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func updateOrganization(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("updateOrganization"), []byte(args[0]), []byte(args[1]), []byte(args[2]), []byte(args[3]), []byte(args[4]), []byte(args[5])})
	if res.Status != shim.OK {
		fmt.Println("updateOrganization failed:", args[0], string(res.Message))
		t.FailNow()
	}
}
func delete(t *testing.T, stub *shimtest.MockStub, args []string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("delete"), []byte(args[0])})
	if res.Status != shim.OK {
		fmt.Println("set failed:", args[0], string(res.Message))
		t.FailNow()
	}
}

func TestInitDemand(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	initDemand(t, stub, []string{"00001", "Milk", "food", "100", "u", "1", "020"})
}
func TestInitProfitablesupply(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	initProfitablesupply(t, stub, []string{"00002", "Milk", "10", "u", "SS", "5"})
}

func TestQueryByKey(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	initUnprofitablesupply(t, stub, []string{"00002", "Milk", "10", "u", "SS"})

	//result := queryByKey(t, stub, []string{"2"})
	//fmt.Println(result)
}

func TestQueryUnproByName(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	initUnprofitablesupply(t, stub, []string{"00002", "Milk", "10", "u", "SS"})
	initUnprofitablesupply(t, stub, []string{"00003", "Milk", "10", "u", "SS"})
	initUnprofitablesupply(t, stub, []string{"00005", "Milk", "10", "u", "SS"})
	initUnprofitablesupply(t, stub, []string{"00004", "Milk", "10", "u", "SS"})
	queryUnproByName(t, stub, []string{"milk"})
}

func TestQueryUnproByOrganization(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	initUnprofitablesupply(t, stub, []string{"00002", "Milk", "10", "u", "SS"})
	queryUnproByOrganization(t, stub, []string{"SS"})
}
func TestUpdateDemandamount(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	initDemand(t, stub, []string{"00001", "Milk", "food", "100", "u", "1", "020"})
	updateDemandamount(t, stub, []string{"00001", "50"})
}

func TestUpdateOrganization(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	initOrganization(t, stub, []string{"1001", "SS", "50", "1", "dd"})

	updateOrganization(t, stub, []string{"1001", "1", "5", "3", "1", "9"})
}
func TestDelete(t *testing.T) {
	cc := new(Chaincode)
	stub := shimtest.NewMockStub("chaincode", cc)
	mockInit(t, stub, nil)
	delete(t, stub, []string{"00001"})
}
