import React from "react";
import { FarmList } from "../data/FarmList";
import { Link, useParams } from "react-router-dom";
import { Card, Container, ListGroup } from "react-bootstrap";
import NavBar from "../components/NavBar";

function FarmDetail() {
	const findId = useParams();
	const farm = FarmList.find((obj) => {
		return obj.koiFarmId == findId.id;
	});

	return (
		<>
			<NavBar />
			<Container style={{ alignItems: "center" }}>
				<Card border="dark" style={{ textAlign: "center" }}>
					<Card.Header as={"h2"}>{farm.name}</Card.Header>
					<Card.Body>
						<Card.Img src={`../${farm.imageUrl}`} alt={farm.name + "img"} />
					</Card.Body>
					<ListGroup variant="flush">
						<ListGroup.Item>{farm.description}</ListGroup.Item>
						<ListGroup.Item>Address: {farm.address}</ListGroup.Item>
						<ListGroup.Item>Email: {farm.email}</ListGroup.Item>
						<ListGroup.Item>Tel: {farm.phone}</ListGroup.Item>
					</ListGroup>
					<Link to={"#"}>Trip that go to this farm</Link>
					<Link to={"#"}>Trip that go to this farm</Link>
					<Link to={"#"}>Trip that go to this farm</Link>
				</Card>
			</Container>
		</>
	);
}

export default FarmDetail;
