import React from "react";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";

function Farm({ farmlist }) {
	return (
		<Container>
			<Row xs={1} lg={2} className="g-4">
				{farmlist.map((farm) => (
					<Col key={farm.koiFarmId}>
						<Card border="dark">
							<Card.Img variant="top" src={farm.imageUrl} alt={farm.name + "img"} />
							<Card.Body style={{ alignItems: "center", textAlign: "center" }}>
								<Card.Title>{farm.name}</Card.Title>
								<Card.Text>{farm.description}</Card.Text>
								<Link to={`/FarmDetail/${farm.koiFarmId}`}>
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

export default Farm;
