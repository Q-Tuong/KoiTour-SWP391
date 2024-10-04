import React from "react";
import { Button, Card, Container, ListGroup } from "react-bootstrap";
import { Link, useParams } from "react-router-dom";
import { KoiList } from "../data/KoiList";

function KoiDetail() {
	const findId = useParams();
	const koi = KoiList.find((obj) => {
		return obj.koiId == findId.id;
	});
	console.log(koi);
	return (
		<Container style={{ alignItems: "center" }}>
			<Card border="dark" style={{ textAlign: "center" }}>
				<Card.Header as={"h2"}>{koi.productCategory}</Card.Header>
				<Card.Body>
					<Card.Img src={`../${koi.imageUrl}`} alt={koi.productCategory + "img"} />
				</Card.Body>
				<ListGroup variant="flush">
					<ListGroup.Item>{koi.description}</ListGroup.Item>
					<ListGroup.Item>Color: {koi.color}</ListGroup.Item>
					<ListGroup.Item>
						Weight: {koi.weight}, Size: {koi.size}{" "}
					</ListGroup.Item>
					<ListGroup.Item>Origin: {koi.origin}</ListGroup.Item>
				</ListGroup>
				<Link to={"#"}>Farm that have this koi</Link>
				<Link to={"#"}>Farm that have this koi</Link>
				<Link to={"#"}>Farm that have this koi</Link>
			</Card>
		</Container>
	);
}

export default KoiDetail;
