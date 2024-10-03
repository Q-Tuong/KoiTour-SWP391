import React from "react";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";

function Orchids({ orchidsList }) {
	return (
		<Container>
			<h1>Orchid list:</h1>
			<Row xs={2} md={3} lg={4} className="g-4">
				{orchidsList.map((orchid) => (
					<Col key={orchid.id}>
						<Card border="dark">
							<Card.Img variant="top" src={orchid.image} />
							<Card.Body style={{ alignItems: "center", textAlign: "center" }}>
								<Card.Title>{orchid.name}</Card.Title>
								<Card.Text>Rating: {orchid.rating}</Card.Text>
								<Button variant="info">
									<Link to={`/OrchidDetail/${orchid.id}`}>Detail</Link>
								</Button>
							</Card.Body>
						</Card>
					</Col>
				))}
			</Row>
		</Container>
	);
}

export default Orchids;
