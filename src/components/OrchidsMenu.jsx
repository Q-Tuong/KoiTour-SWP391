import React from "react";
import { data } from "../data/ListOfOrchids";
import Orchids from "./Orchids";

function OrchidsMenu() {
	return <Orchids orchidsList={data} />;
}

export default OrchidsMenu;
