import React from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import { TourList } from "../data/TourList";
import { useParams } from "react-router-dom";
import NavBar from "../components/NavBar";

function TourDetail() {
	const findId = useParams();
	const tour = TourList.find((obj) => {
		return obj.tourId == findId.id;
	});
	console.log(tour);

	return (
		<>
			<NavBar />
			<Container>
				<Row>{tour.name}'s Details:</Row>
				<Row>{tour.description}</Row>
				<Row>
					<Col>Tour type: {tour.typeId}</Col>
					<Col>Price: {tour.price}</Col>
				</Row>
				<Row>
					<Col>Start Date: {tour.dateStart}</Col>
					<Col>End Date: {tour.dateEnd}</Col>
				</Row>
				<Button variant="primary">Book now</Button>
			</Container>
		</>
	);
}

export default TourDetail;
