import React from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";

function Contact() {
	return (
		<Container>
			<h1>Contact Us</h1>
			<Form>
				<Row className="mb-3">
					<Form.Group as={Col} controlId="firstname-control">
						<Form.Label>First Name:</Form.Label>
						<Form.Control type="text" placeholder="First name" />
					</Form.Group>

					<Form.Group as={Col} controlId="lastname-control">
						<Form.Label>Last Name:</Form.Label>
						<Form.Control type="text" placeholder="Last name" />
					</Form.Group>
				</Row>
				<Form.Group className="mb-3" controlId="email-control">
					<Form.Label>Email:</Form.Label>
					<Form.Control type="email" placeholder="abcxyz@email.com" />
				</Form.Group>

				<Form.Group className="mb-3" controlId="comment-control">
					<Form.Label>Comment:</Form.Label>
					<Form.Control as="textarea" placeholder="Your comment" rows={2} />
				</Form.Group>

				<Button type="submit" variant="secondary">
					Submit
				</Button>
			</Form>
		</Container>
	);
}

export default Contact;
