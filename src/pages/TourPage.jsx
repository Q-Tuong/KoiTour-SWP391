import React from "react";
import { Container } from "react-bootstrap";
import Tour from "../components/Tour";
import { TourList } from "../data/TourList";
import NavBar from "../components/NavBar";

function TourPage() {
	return (
		<>
			<NavBar />
			<Container>
				<h2>Koi Farm Tour: </h2>
				<Tour tourlist={TourList} />
			</Container>
		</>
	);
}

export default TourPage;
