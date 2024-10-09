import React from "react";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";

function Koi({ koilist }) {
	return (
		<Container>
			<Row xs={2} md={3} lg={4} className="g-4">
				{koilist.map((koi) => (
					<Col key={koi.koiId}>
						<Card border="dark">
							<Card.Img variant="top" src={koi.imageUrl} alt={koi.productCategory + "image"} />
							<Card.Body style={{ alignItems: "center", textAlign: "center" }}>
								<Card.Title>{koi.productCategory}</Card.Title>
								<Card.Text>Weight: {koi.weight}</Card.Text>
								<Card.Text>Size: {koi.size}</Card.Text>
								<Link to={`/KoiDetail/${koi.koiId}`}>
									<Button variant="info">Detail</Button>
								</Link>
							</Card.Body>
						</Card>
					</Col>
				))}
			</Row>
		</Container>
	);
}

export default Koi;
