import React from "react";
import { data } from "../data/ListOfOrchids";
import Orchids from "./Orchids";

function OriginalOrchids() {
	const originalList = data.filter((orchid) => orchid.isSpecial === true);

	return <Orchids orchidsList={originalList} />;
}

export default OriginalOrchids;
