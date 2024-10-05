import React from "react";
import Farm from "../components/Farm";
import { FarmList } from "../data/FarmList";
import { Container } from "react-bootstrap";
import NavBar from "../components/NavBar";

function FarmPage() {
	return (
		<>
			<NavBar />
			<Container>
				<h2>Our Koi Farm: </h2>
				<Farm farmlist={FarmList} />
			</Container>
		</>
	);
}

export default FarmPage;
