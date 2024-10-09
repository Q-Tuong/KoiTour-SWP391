import React, { useState } from "react";
import { Button, Card, Col, Container, Image, ListGroup, Row } from "react-bootstrap";
import { Link, useParams } from "react-router-dom";
import { KoiList } from "../data/KoiList";
import NavBar from "../components/NavBar";
import UpdateKoi from "../components/UpdateKoi";

function KoiDetail() {
	const [isOpen, setIsOpen] = useState(false);

	const findId = useParams();
	const koi = KoiList.find((obj) => {
		return obj.koiId == findId.id;
	});
	console.log(koi);
	return (
		<>
			<NavBar />
			<Container style={{ alignItems: "center" }}>
				<Row as={"h2"}>Koi Detail</Row>
				<Card border="dark" className="col-sm-10 offset-sm-1" style={{ textAlign: "center" }}>
					<Card.Header as={"h2"}>{koi.productCategory}</Card.Header>
					{/* <Card.Body>
						<Card.Img src={`../${koi.imageUrl}`} alt={koi.productCategory + "img"} />
					</Card.Body>
					<ListGroup variant="flush">
						<ListGroup.Item>{koi.description}</ListGroup.Item>
						<ListGroup.Item>Color: {koi.color}</ListGroup.Item>
						<ListGroup.Item>
							Weight: {koi.weight}, Size: {koi.size}{" "}
						</ListGroup.Item>
						<ListGroup.Item>Origin: {koi.origin}</ListGroup.Item>
					</ListGroup> */}
					<Row className="g-0">
						<Col className="md-4">
							<Image src={`../${koi.imageUrl}`} fluid alt={koi.productCategory + "img"} />
						</Col>
						<Col className="md-8">
							<Card.Body>
								<Card.Text>{koi.description}</Card.Text>
								<ListGroup variant="flush">
									<ListGroup.Item>Color: {koi.color}</ListGroup.Item>
									<ListGroup.Item>Weight: {koi.weight}</ListGroup.Item>
									<ListGroup.Item>Size: {koi.size}</ListGroup.Item>
									<ListGroup.Item>Origin: {koi.origin}</ListGroup.Item>
								</ListGroup>
							</Card.Body>
						</Col>
					</Row>
					<Card.Footer>
						<Link to={"#"}>Farm that have this koi</Link>
						<Button onClick={() => setIsOpen(true)}>Update</Button>
					</Card.Footer>
				</Card>
				{isOpen && <UpdateKoi setIsOpen={setIsOpen} open={isOpen} koi={koi} />}
			</Container>
		</>
	);
}

export default KoiDetail;
