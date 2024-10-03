import React, { useState } from "react";
import { Button, Card, Col, Container, ListGroup, Row } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { data } from "../data/ListOfOrchids";
import OrchidVideo from "./OrchidVideo";

function OrchidDetail() {
	const orchidId = useParams();
	const orchid = data.find((obj) => {
		return obj.id == orchidId.id;
	});

	const [isOpen, setIsOpen] = useState(false);

	return (
		<Container style={{ alignItems: "center" }}>
			<Card border="dark" style={{ textAlign: "center" }}>
				<Card.Header as={"h2"}>{orchid.name}</Card.Header>
				<Card.Body>
					<Card.Img src={`../${orchid.image}`} />
					<Button onClick={() => setIsOpen(true)} variant="info">
						Video
					</Button>
					{isOpen && <OrchidVideo setIsOpen={setIsOpen} open={isOpen} orchid={orchid} />}
				</Card.Body>
				<ListGroup variant="flush">
					<ListGroup.Item>Rating: {orchid.rating}</ListGroup.Item>
					<ListGroup.Item>Color: {orchid.color}</ListGroup.Item>
					<ListGroup.Item>Category: {orchid.category}</ListGroup.Item>
					<ListGroup.Item>Origin: {orchid.origin}</ListGroup.Item>
				</ListGroup>
			</Card>
		</Container>
	);
}

export default OrchidDetail;
